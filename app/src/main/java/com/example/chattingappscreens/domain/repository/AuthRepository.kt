package com.example.chattingappscreens.domain.repository

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithEmail(email : String , password : String) : Flow<ResultState<String>>
    fun signUpWithEmail(user: UserModel) : Flow<ResultState<String>>
    fun loginWithGoogle(idToken : String) : Flow<ResultState<String>>
    suspend fun logOut(uid : String) : Boolean
    suspend fun updateUserProfile(uid: String, imageUri: String): Result<Boolean>
    suspend fun updateOnlineStatus(isOnline: Boolean, uid: String): Boolean
    suspend fun deleteUser(uid: String): Boolean
    suspend fun updateUser(uid : String ,name : String , phoneNo : String ): Result<String>
}



