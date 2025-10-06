package com.example.chattingappscreens.data.repository

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.ChatModel
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HomeRepoImpl(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth,
//    private val firebaseStorage: FirebaseStorage
) : HomeRepository {



    override suspend fun getAllUsers(): ResultState<List<UserModel>> {                         //for suggestions bro
        ResultState.Loading
        return try {

            val snapshot = firebaseDatabase.getReference("User").get().await()
            if (snapshot.exists()) {
                val list = mutableListOf<UserModel>()
                for (user in snapshot.children) {
                    val userMap = user.value as Map<String, Any>
                    list.add(UserModel.mapToUser(userMap))
                }
                ResultState.Success(list)
            } else {
                ResultState.Error("Error :Users Not Found")
            }
        } catch (e: Exception) {
            ResultState.Error("SearchFailed : $e")
        }
    }


    override suspend fun searchUsers(name: String, myUid: String): ResultState<List<UserModel>> {
        return try {

            val friendsUidList = getAllFriends(myUid =myUid)
               val list = mutableListOf<UserModel>()

                val snapshot = firebaseDatabase.getReference("User").get().await()

            val queryFormated = name.lowercase()

                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val user = child.value as Map<String, Any>
                        val mapedUser = UserModel.mapToUser(user)
                        val usersNameFormated = mapedUser.name.lowercase()

                        if (mapedUser.uid.isNotEmpty() && mapedUser.uid != myUid && !friendsUidList.contains(
                                mapedUser.uid)
                        ){
                            if (
                                usersNameFormated.startsWith(queryFormated, ignoreCase = true) ||
                                usersNameFormated.contains(queryFormated, ignoreCase = true) ||
                                usersNameFormated.endsWith(queryFormated, ignoreCase = true) ||
                                queryFormated.contains(usersNameFormated.take(3))
                            ) {
                                list.add(mapedUser)
                            }
                        }
                    }
            }
                ResultState.Success(list)


        } catch (e: Exception) {
            ResultState.Error("Search Error:$e")
        }

    }


    private suspend fun getAllFriends(myUid : String) : List<String> {

        val ref = firebaseDatabase.getReference("User_chats").child(myUid).get().await()

       val friendUidList = ref.children.mapNotNull { snap ->
            val chatModel = snap.getValue(ChatModel::class.java)
          chatModel?.participants?.firstOrNull{it != myUid}
       }

        return friendUidList

    }


    override suspend fun getUserById(uid: String): ResultState<UserModel> {
        ResultState.Loading

        return try {
            val snapshot =
                firebaseDatabase.getReference("User").child(uid).get().await()

            if (snapshot.exists()) {
                val user = snapshot.value as Map<String, Any>
                ResultState.Success(UserModel.mapToUser(user))
            } else {
                ResultState.Error("UserNotFound !!!")
            }


        }catch (e : Exception){
            ResultState.Error("Error :$e")
        }

    }


}