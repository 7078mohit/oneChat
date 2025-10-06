package com.example.chattingappscreens.data.modell

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    var messageId : String = "",
    val chatId : String = "",
    val content : String = "",
    val receiverId : String = "",
    val senderId : String = "",
    val messageType : MessageType = MessageType.TEXT,
    val timeStamp : Long = System.currentTimeMillis(),
    val isRead : Boolean = false,
    val mediaUrl : String = "",
    val mediaThumbnailUrl : String = "",
    val replyToMessageId : String = "",
    val isForwarded : Boolean = false,
    val isEdited : Boolean = false,
    val editedAt : Long = 0L
    ){

    constructor() : this(
        messageId = "",
        chatId = "",
        content = "",
        receiverId = "",
        senderId= "",
        messageType = MessageType.TEXT,
        timeStamp = System.currentTimeMillis(),
        isRead = false,
        mediaUrl = "",
        mediaThumbnailUrl = "",
        replyToMessageId = "",
        isForwarded = false,
        isEdited = false,
        editedAt = 0L,
    )


    fun toMap() : Map<String , Any>{
        return mapOf(
            "messageId" to messageId,
            "chatId" to chatId,
            "content" to content,
            "receiverId" to receiverId,
            "senderId" to senderId,
            "messageType" to messageType,
            "timeStamp" to timeStamp,
            "isRead" to isRead,
            "mediaUrl" to mediaUrl,
            "mediaThumbnailUrl" to mediaThumbnailUrl,
            "replyToMessageId" to replyToMessageId,
            "isForwarded" to isForwarded,
            "isEdited" to isEdited,
            "editedAt" to editedAt
        )
    }

    companion object{
        fun mapToMessage( map : Map<String , Any>) : MessageModel{
            return MessageModel(
                messageId = map["messageId"] as? String ?: "",
                chatId = map["chatId"] as? String ?: "",
                content = map["content"] as? String ?: "",
                receiverId = map["receiverId"] as? String ?: "",
                senderId =  map["senderId"] as? String ?: "",
                messageType = MessageType.valueOf(map["messageType"] as? String ?: "TEXT"),
                timeStamp = map["timeStamp"] as? Long ?: System.currentTimeMillis() ,
                isRead = map["isRead"] as? Boolean ?: false,
                mediaUrl = map["mediaUrl"] as? String ?: "",
                mediaThumbnailUrl = map["mediaThumbnailUrl"] as? String ?: "",
                replyToMessageId = map["replyToMessageId"] as? String ?: "",
                isForwarded = map["isForwarded"] as? Boolean ?: false,
                isEdited = map["isEdited"] as? Boolean ?: false,
                editedAt = map["editedAt"] as? Long ?: 0L
            )
        }
    }
}







enum class MessageType{
    TEXT,
    AUDIO,
    VIDEO,
    DOCUMENT,
    IMAGE,
    EMOJI,
    CONTACT,
    LOCATION
}


