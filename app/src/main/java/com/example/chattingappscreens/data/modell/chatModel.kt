package com.example.chattingappscreens.data.modell

import kotlinx.serialization.Serializable

@Suppress("UNCHECKED_CAST")
@Serializable
data class ChatModel(
    val chatId : String = "",
    val participants : List<String> = emptyList(),
    val lastMessage : String  = "",
    val messageId : String = "",
    val lastMessageTime : Long = 0L,
    val lastMessageSenderId : String = "",
    val unreadCount : Map<String , Long> = emptyMap(),                  // isme both users ke unreadCounts honge
    val isGroup : Boolean = false,
    val groupName : String = "",
    val groupImageUrl : String = "",
    val createdBy : String = "",
    val createdAt : Long = System.currentTimeMillis()
){
    constructor() : this(
     chatId  = "",
     participants   = emptyList(),
     lastMessage = "",
     messageId = "",
     lastMessageTime = 0L,
     lastMessageSenderId = "",
     unreadCount  = emptyMap(),
     isGroup = false ,
     groupName = ""  ,
     groupImageUrl = "",
     createdBy = "",
     createdAt = System.currentTimeMillis()
    )


    fun toMap() : Map<String , Any> {
       return mapOf(
        "chatId" to chatId,
        "participants" to  participants,
        "lastMessage" to lastMessage,
        "messageId" to messageId,
        "lastMessageTime" to lastMessageTime,
        "lastMessageSenderId" to lastMessageSenderId,
        "unreadCount" to unreadCount,
        "isGroup" to isGroup,
        "groupName" to groupName,
        "groupImageUrl" to groupImageUrl,
        "createdBy" to createdBy,
        "createdAt" to createdAt
        )
    }

    companion object{
        fun mapToChat(map : Map<String , Any>): ChatModel{

            // safe cast ok bro thoda tricky h but best
            val unreadCount = (map["unreadCount"] as? Map<*, *>)?.mapNotNull { (key, value) ->
                val safeKey = key as? String ?: return@mapNotNull null
                val safeValue = when (value) {
                    is Number -> value.toLong()
                    is String -> value.toLongOrNull() ?: 0L
                    else -> 0L
                }
                safeKey to safeValue
            }?.toMap() ?: emptyMap()

            return ChatModel(
                chatId = map["chatId"] as? String ?: "",
                participants = (map["participants"] as? List<String>) ?: emptyList(),
                lastMessage = map["lastMessage"] as? String ?: "",
                messageId = map["messageId"] as? String ?: "",
                lastMessageTime = map["lastMessageTime"] as? Long ?: 0L,
                lastMessageSenderId = map["lastMessageSenderId"] as? String ?: "",
                unreadCount = unreadCount,                                      // modified hai ye bro
                isGroup = map["isGroup"] as? Boolean ?: false,
                groupName = map["groupName"] as? String ?: "",
                groupImageUrl = map["groupImageUrl"] as? String ?: "",
                createdBy = map["createdBy"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
            )
        }
    }

}