package com.example.chattingappscreens.data.modell

import kotlinx.serialization.Serializable

//
//data class  SignRequest(
//    val name : String = "",
//    val phone : String = "",
//    val email : String = "",
//    val password : String = "",
//    val profile : String = "",
//)

@Serializable
data class UserModel(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val profile: String = "",
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val fcmToken: String = ""
){

    fun toMap() : Map<String , Any>{
        return mapOf(
            "uid" to  uid ,
            "name" to name,
            "email" to email,
            "phone" to phone,
            "profile" to profile,
            "isOnline" to isOnline,
            "lastSeen" to lastSeen,
            "fcmToken" to fcmToken
        )
    }

    companion object{

    fun mapToUser(map : Map<String , Any>) : UserModel{
        return UserModel(
             uid = map["uid"]  as? String ?: "",
             name = map["name"] as? String ?: "",
            email = map["email"] as? String ?: "",
            phone = map["phone"] as? String ?: "",
            profile = map["profile"] as? String ?: "",
            isOnline = map["isOnline"] as? Boolean ?: false,
            lastSeen = map["lastSeen"] as? Long ?: System.currentTimeMillis(),
            fcmToken = map["fcmToken"] as? String ?: ""
        )
    }
}
}