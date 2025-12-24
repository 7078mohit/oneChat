package com.example.chattingappscreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.core.preference.PreferenceData
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.usecase.Auth.DeleteUserUseCase
import com.example.chattingappscreens.domain.usecase.Auth.LogOutUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SignInWithEmailUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SignUpWithEmailUseCase
import com.example.chattingappscreens.domain.usecase.Auth.SigningWithGoogleUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateOnlineStatusUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateUserProfileUseCase
import com.example.chattingappscreens.domain.usecase.Auth.UpdateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AuthViewModel(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signingWithGoogleUseCase: SigningWithGoogleUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val updateOnlineStatusUseCase: UpdateOnlineStatusUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    preferenceData: PreferenceData,                                               //ye preference ko ham  viewmodel ke fun me use nahi kr skte ab , agr fun me use krna ha to issko private val krna hoga
    private val firebaseAuth: FirebaseAuth,
//    private val firebaseStorage: FirebaseStorage,
//    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {


    fun getCurrentUserId() = firebaseAuth.currentUser?.uid

    val uidPreference = preferenceData.uid.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val namePreference = preferenceData.name.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val emailPreference = preferenceData.email.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val phonePreference = preferenceData.phone.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val isLoggedPreference = preferenceData.loggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true   //checking
    )
    val profilePreference = preferenceData.profile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val isOnlinePreference = preferenceData.isOnline.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false  //checking ke liye hai
    )
    val lastSeenPreference = preferenceData.lastSeen.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    val fcmTokenPreference = preferenceData.fcmToken.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )


    private val _signInState = MutableStateFlow(SignInWithEmailState())
    val signInState = _signInState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignUpWithEmailState())
    val signUpState = _signUpState.asStateFlow()

    private val _signingState = MutableStateFlow(SigningWithGoogleState())
    val signingState = _signingState.asStateFlow()

    private val _logOutState = MutableStateFlow(LogoutState())
    val logoutState = _logOutState.asStateFlow()

    private val _updateUserProfileState = MutableStateFlow(UpdateUserProfileState())
    val updateUserProfileState = _updateUserProfileState.asStateFlow()

    private val _deleteUserState = MutableStateFlow(DeleteUserState())
    val deleteUserState = _deleteUserState.asStateFlow()

    private val _updateUserState = MutableStateFlow(UpdateUserState())
    val updateUserState = _updateUserState.asStateFlow()


    fun signInWithEmail(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            signInWithEmailUseCase(email = email, password = password).collect { signInResult ->
                when (signInResult) {

                    is ResultState.Loading -> {
                        _signInState.value =
                            SignInWithEmailState(loading = true, success = null, error = null)
                    }

                    is ResultState.Error -> {
                        _signInState.value =
                            SignInWithEmailState(error = signInResult.error, loading = false)
                    }

                    is ResultState.Success -> {
                        _signInState.value =
                            SignInWithEmailState(success = signInResult.data, loading = false)
                    }
                }

            }
        }
    }

    fun signUpWithEmail(user: UserModel) {

        viewModelScope.launch(Dispatchers.IO) {
            signUpWithEmailUseCase(user = user).collect { signUpResult ->

                when (signUpResult) {

                    is ResultState.Loading -> {
                        _signUpState.value =
                            SignUpWithEmailState(loading = true, success = null, error = null)
                    }

                    is ResultState.Error -> {
                        _signUpState.value =
                            SignUpWithEmailState(error = signUpResult.error, loading = false)
                    }

                    is ResultState.Success -> {
                        _signUpState.value =
                            SignUpWithEmailState(success = signUpResult.data, loading = false)
                    }
                }
            }
        }
    }


    fun signIngWithGoogle(idToken: String) {
        viewModelScope.launch {
            signingWithGoogleUseCase(idToken = idToken).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _signingState.value =
                            SigningWithGoogleState(loading = true, success = null, error = null)
                    }

                    is ResultState.Error -> {
                        _signingState.value =
                            SigningWithGoogleState(loading = false, error = it.error)
                    }

                    is ResultState.Success -> {
                        _signingState.value =
                            SigningWithGoogleState(loading = false, success = it.data)
                    }
                }
            }
        }
    }

    fun logOut() {
        _logOutState.value = LogoutState(isLoading = true, isSuccess = false)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = logOutUseCase(uid = getCurrentUserId() ?: "")
                if (result) {
                    _logOutState.value = LogoutState(isSuccess = true, isLoading = false)
                    _signingState.value = SigningWithGoogleState()
                    _signInState.value = SignInWithEmailState()
                    _signUpState.value = SignUpWithEmailState()
                } else {
                    _logOutState.value =
                        LogoutState(
                            isError = "logout failed!",
                            isSuccess = false,
                            isLoading = false
                        )
                }
            }catch (e: Exception){
                _logOutState.value = LogoutState(
                    isError = "Logout failed: ${e.message}",
                    isLoading = false
                )
            }

        }
    }

    fun updateUserProfile(imageUri: String) {
        _updateUserProfileState.value = UpdateUserProfileState(isLoading = true, isSuccess = false)
        viewModelScope.launch {
            val resultState =
                updateUserProfileUseCase(uid = getCurrentUserId() ?: "", imageUri = imageUri)
            when {
                resultState.isSuccess -> {
                    _updateUserProfileState.value =
                        UpdateUserProfileState(isSuccess = true, isError = null, isLoading = false)
                }

                resultState.isFailure -> {
                    _updateUserProfileState.value =
                        UpdateUserProfileState(
                            isLoading = false,
                            isError = "Error :${resultState.exceptionOrNull()}",
                            isSuccess = false
                        )
                }

            }
        }
    }


    fun updateOnlineStatus(isOnline: Boolean) {                       // iske liye koi state ni bnai
        viewModelScope.launch(Dispatchers.IO) {
            updateOnlineStatusUseCase(isOnline = isOnline, uid = getCurrentUserId() ?: "")
        }
    }

    fun deleteUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteUserUseCase(uid = getCurrentUserId() ?: "")
            if (result) {
                _deleteUserState.value = DeleteUserState(isSuccess = true)
            } else {
                _deleteUserState.value =
                    DeleteUserState(isError = "Delete User Failed!", isSuccess = false)
            }
        }
    }

    fun updateUser(uid: String, name: String, phoneNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserState.value =
                UpdateUserState(isLoading = true, isSuccess = false, isError = null)
            val result = updateUserUseCase(uid = uid, name = name, phoneNo = phoneNo)
            when {
                result.isSuccess -> {
                    _updateUserState.value = UpdateUserState(
                        isLoading = false,
                        isSuccess = true,
                        isError = null
                    )
                }

                result.isFailure -> {
                    _updateUserState.value = UpdateUserState(
                        isLoading = false,
                        isError = result.getOrThrow(),
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun resetSignInState() {
        _signInState.value = SignInWithEmailState()
    }

}

data class UpdateUserState(
    var isLoading: Boolean? = false,
    var isSuccess: Boolean? = false,
    var isError: String? = null
)

data class DeleteUserState(
    val isSuccess: Boolean? = false,
    val isError: String? = null
)

data class UpdateUserProfileState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false,
    val isError: String? = null
)

data class LogoutState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false,
    val isError: String? = null
)

data class SignInWithEmailState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)

data class SignUpWithEmailState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)

data class SigningWithGoogleState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)
