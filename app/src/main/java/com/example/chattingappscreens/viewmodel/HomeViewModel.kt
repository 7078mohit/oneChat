package com.example.chattingappscreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.usecase.Home.GetAllUsersUserCase
import com.example.chattingappscreens.domain.usecase.Home.GetUserByIdUseCase
import com.example.chattingappscreens.domain.usecase.Home.SearchUserUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(
  private val getAllUsersUserCase: GetAllUsersUserCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val searchUserUseCase: SearchUserUseCase,
    private val firebaseAuth: FirebaseAuth
)  : ViewModel(){

    val myUid  = firebaseAuth.currentUser?.uid

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    private var job : Job? = null

    private val _getAllUserState  =  MutableStateFlow(GetAllUserState())
    val getAllUserState = _getAllUserState.asStateFlow()

    private val _getUserByIdState = MutableStateFlow(GetUserByIdState())
    val getUserByIdState = _getUserByIdState.asStateFlow()

    private val _searchUserByName = MutableStateFlow(SearchUserState())
    val searchUserByName = _searchUserByName.asStateFlow()

    fun getAllUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val users = getAllUsersUserCase.invoke()
            when(users){
                is ResultState.Loading -> {
                  _getAllUserState.value =  GetAllUserState(isLoading = true , isSuccess =  null)
                }
                is ResultState.Success-> {
                   _getAllUserState.value = GetAllUserState(isSuccess = users.data , isLoading = false, isError = null)
                }
                is ResultState.Error -> {
                   _getAllUserState.value = GetAllUserState(isError = users.error , isLoading = false,
                       isSuccess = null)
                }
            }
        }
    }

    fun getUserById(uid : String){
        viewModelScope.launch(Dispatchers.IO) {
            val users = getUserByIdUseCase.invoke(uid = uid)
            when(users){
                is ResultState.Loading -> {
                 _getUserByIdState.value =   GetUserByIdState(isLoading = true , isSuccess = null, isError = null)
                }
                is ResultState.Success-> {
                  _getUserByIdState.value =  GetUserByIdState(isSuccess = users.data , isLoading = false, isError = null)
                }
                is ResultState.Error -> {
                    _getUserByIdState.value = GetUserByIdState(isError = users.error , isLoading = false, isSuccess = null)
                }
            }
        }
    }

    init {
        searchDecoration()
    }


    @OptIn(FlowPreview::class)
    fun searchDecoration(){
        viewModelScope.launch(Dispatchers.IO) {
            _searchText
                .debounce(1000)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { text ->
                    getSearchUser( name =text )
                }
        }
    }



    fun getSearchUser(name : String ){

        job?.cancel()
        job =  viewModelScope.launch(Dispatchers.IO) {
            _searchUserByName.value =  SearchUserState(isLoading = true , isSuccess = null, isError = null)

            val user = searchUserUseCase.invoke(name = name , myUid = myUid ?: "")
            when(user){
                is ResultState.Error -> {
                  _searchUserByName.value =  SearchUserState(isError = user.error , isSuccess = null, isLoading = false)
                }
                is ResultState.Success -> {
                   _searchUserByName.value = SearchUserState(isSuccess =  user.data , isLoading = false, isError = null)
                }
                else -> Unit
            }
        }
    }

    fun updateText(text : String ){
        _searchText.value = text
    }



}

data class GetAllUserState(
  val isLoading : Boolean = false,
    val isSuccess : List<UserModel>? = null,
    val isError : String ? = null
)
data class GetUserByIdState(
  val isLoading : Boolean = false,
    val isSuccess : UserModel? = null,
    val isError : String ? = null
)
data class SearchUserState(
  val isLoading : Boolean = false,
    val isSuccess : List<UserModel>? = null,
    val isError : String ? = null
)
