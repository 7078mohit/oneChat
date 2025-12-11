package com.example.chattingappscreens.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.core.preference.PreferenceData
import com.example.chattingappscreens.core.utils.compressedImageWithUri
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepoImpl(
        private var firebaseAuth : FirebaseAuth,
        private var firebaseDatabase : FirebaseDatabase,
        private var firebaseStorage : FirebaseStorage,
        private  var firebaseMessaging: FirebaseMessaging,
        private var preferenceData: PreferenceData,
        private var context : Context) : AuthRepository
{

    override fun signInWithEmail(
        email: String,
        password: String
    ): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            val uid = result.user?.uid ?: throw Exception("User Id null!")

            // Save to preferencee
            val user = getUserFromDB(uid)                       // manually find for save in datastorePrefernce
            if (user != null){
                preferenceData.saveUser(
                    uid = user.uid,
                    name = user.name,
                    phone = user.phone,
                    email = user.email,
                    profile = user.profile,
                    isOnline = user.isOnline,
                    lastSeen = user.lastSeen,
                    fcmToken = user.fcmToken,
                )
            }

            emit(ResultState.Success(uid))
        } catch (e: Exception) {
            emit(ResultState.Error("Failed :${e.localizedMessage}"))
        }
    }


    @OptIn(InternalCoroutinesApi::class)
    override fun signUpWithEmail(
        user: UserModel
    ): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val resultState =
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                    .await()


            val uid = resultState.user?.uid ?: throw IllegalArgumentException("Uid Null")
            val token = firebaseMessaging.token.await()
            val url = saveProfileInStorage(user.profile.toUri())


            val finalUser = user.copy(
                uid = uid,
                profile = url ?: "",
                fcmToken = token ?: ""
            )

            saveUserToRealtimeDB(finalUser)

            preferenceData.saveUser(
                uid = finalUser.uid,
                name = finalUser.name,
                phone = finalUser.phone,
                email = finalUser.email,
                profile = finalUser.profile,
                isOnline = finalUser.isOnline,
                lastSeen = finalUser.lastSeen,
                fcmToken = token ?: "",
            )
            emit(ResultState.Success(uid))                      //success
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "SignUp Failed"))
        }
    }


    private suspend fun saveUserToRealtimeDB(user: UserModel) {
        try {
            val uid = user.uid
            if (uid.isEmpty()) {
                throw IllegalArgumentException("Uid Empty!!")
            }

            val reference = firebaseDatabase.getReference("User").child(user.uid)
            val result = reference.setValue(user.toMap()).await()
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun saveProfileInStorage(uri: Uri): String? {
       return  try {

           val compressedUri = compressedImageWithUri(context = context  , uri = uri)  ?: throw IllegalArgumentException("Uri null Compressed Error")

            val path = "Image_${System.currentTimeMillis()}_.jpg"
            val imageReference = firebaseStorage.getReference("Profile").child(path)
            imageReference.putFile(compressedUri ).await()
           val url = imageReference.downloadUrl.await()

           Log.d("upload image success" , "$url")

           url.toString()
       } catch (e : Exception){
           Log.d("upload image Failed" ,"uploadfailed", e)
            throw e

        }
    }


    override fun loginWithGoogle(idToken : String): Flow<ResultState<String>> = flow {

        emit(ResultState.Loading)
        try {
        val authCredential =  GoogleAuthProvider.getCredential(idToken , null)
        val authResult = firebaseAuth.signInWithCredential(authCredential).await()
            val token = firebaseMessaging.token.await()

        val user = authResult.user ?: throw IllegalArgumentException("User null")

            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            val profile = user.photoUrl
            Log.d("profile" , profile.toString())


            /// Google se user mil gaya
            val googleUser = UserModel(
                uid = user.uid,
                name = user.displayName ?: "",
                phone = user.phoneNumber ?: "",
                email = user.email ?: "",
                profile = profile.toString(),
                fcmToken = token ?: ""
            )

            if (isNewUser) {
                saveUserToRealtimeDB(user = googleUser)
                preferenceData.saveUser(
                    uid = googleUser.uid,
                    name = googleUser.name,
                    phone = googleUser.phone,
                    email = googleUser.email,
                    profile = googleUser.profile,
                    isOnline = true,
                    lastSeen = googleUser.lastSeen,
                    fcmToken = token ?: "",
                )
            }
            else{
               val addedUser = getUserFromDB(user.uid)

                preferenceData.saveUser(
                    uid = addedUser?.uid ?: "",
                    name = addedUser?.name ?: "",
                    phone = addedUser?.phone ?: "",
                    email = addedUser?.email ?: "",
                    profile = addedUser?.profile ?: "",
                    isOnline = true,
                    fcmToken = addedUser?.fcmToken ?: "",
                )
            }




            emit(ResultState.Success(user.uid))

        }
        catch (e : Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Login with google failed"))
        }
    }

    override suspend fun logOut(uid: String): Boolean {
      return  try {
        val statusResult = updateOnlineStatus(isOnline = false , uid = uid)
        if (statusResult){
            firebaseAuth.signOut()
            preferenceData.deleteUser()
            true
        }else{
            false
        }
    }
        catch (e: Exception){
        false
        }
    }


    override suspend fun  updateOnlineStatus(isOnline : Boolean, uid: String) : Boolean {
       return try {

           val currentTime  = System.currentTimeMillis()

            val map = mapOf(
                "isOnline" to isOnline,
                "lastSeen" to currentTime
            )

            firebaseDatabase.getReference("User")
                .child(uid)
                .updateChildren(map)
                .await()
           preferenceData.onlineLastSeen(isOnline = isOnline , lastSeen = currentTime )

            true
        }catch (e : Exception){
            false
        }

    }

    suspend fun getUserFromDB(uid : String) : UserModel? {
      return  try {
          val snapshot = firebaseDatabase.getReference("User").child(uid).get().await()
          // val x = snapshot.value  as Map<String , Any>

          if (snapshot.exists()) {
              val map = snapshot.value as Map<String, Any>
              UserModel.mapToUser(map)
          } else {
             null
          }
      }
        catch (e: Exception){
          throw  e
        }
    }

   override suspend fun updateUserProfile(uid : String, imageUri : String)  : Result<Boolean> {
       return try {
           val imageUrl = saveProfileInStorage(uri = imageUri.toUri())
            imageUrl?.let {
        firebaseDatabase
            .getReference("User")
            .child(uid)
            .child("profile")
            .setValue(imageUrl)
            .await()
            preferenceData.updateProfile(imageUrl = imageUri)
            }
           Result.success(true)
    }
        catch (e : Exception){
            Result.failure(e)
        }
   }



   override suspend fun deleteUser(uid: String) : Boolean{
      return  try {
        firebaseAuth.currentUser?.delete()?.await()
        firebaseDatabase.getReference("User").child(uid).removeValue().await()
        firebaseStorage.getReference("Image")
            true
    }catch (e: Exception){
        false
    }
    }


    override suspend fun updateUser(uid : String ,name : String , phoneNo : String) : Result<String>{
       return try {

//           val imageUrl = saveProfileInStorage(uri = user.profile.toUri())

           val map = mapOf(
               "name" to name,
               "phone" to phoneNo,
           )

        firebaseDatabase.getReference("User").child(uid)
            .updateChildren(map)
            .await()


           // isme pura hi pas kr diya nahi to preference me fun add krna pdta one more
           preferenceData.updateUser(
               uid = uid,
               name = name,
               phone = phoneNo,
           )

            Result.success("User Updated")
    }catch (e: Exception){
            Result.failure(e)
    }
    }




}