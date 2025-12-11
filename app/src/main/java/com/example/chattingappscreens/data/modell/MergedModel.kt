package com.example.chattingappscreens.data.modell

data class MergedModel(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val profile: String = "",
    val chatId: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val lastMessageSenderId: String = "",
    val unreadCount: Map<String, Long> = emptyMap(),
    val isGroup: Boolean = false,
    val groupName: String = "",
    val groupImageUrl: String = "",
    val messageId : String = ""
    ){

     constructor() : this(
         uid = "",
         name = "",
         phone = "",
         email = "",
         profile =  "",
         chatId = "",
         lastMessage = "",
         lastMessageTime = System.currentTimeMillis(),
         lastMessageSenderId = "",
         unreadCount = emptyMap(),
         isGroup = false,
         groupName = "",
         groupImageUrl = "",
         messageId = ""
     )

    fun toMap() : Map<String , Any>{
        return mapOf(
            "uid" to uid,
            "name" to name,
            "phone" to phone,
            "email" to email,
            "profile" to profile,
            "chatId" to chatId,
            "lastMessage" to lastMessage,
            "lastMessageTime" to lastMessageTime,
            "lastMessageSenderId" to lastMessageSenderId,
            "unreadCount" to unreadCount,
            "isGroup" to isGroup,
            "groupName" to groupName,
            "groupImageUrl" to groupImageUrl,
            "messageId" to messageId
        )
    }

    companion object {
        fun mapToModel(map : Map<String , Any>) {
        }

    }

}