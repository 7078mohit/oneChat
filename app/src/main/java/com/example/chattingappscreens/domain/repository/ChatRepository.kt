package com.example.chattingappscreens.domain.repository

import com.example.chattingappscreens.data.modell.MergedModel
import com.example.chattingappscreens.data.modell.MessageModel
import com.example.chattingappscreens.data.modell.UserModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChat(userId: String): Flow<Result<List<MergedModel>>>
    suspend fun createChat(
        participants: List<String>,
        isGroup: Boolean,
        groupName: String
    ): Result<String>

    suspend fun getFriendByUid(uid: String): Result<UserModel?>
    suspend fun sendMessage(message: MessageModel, thumbnail: ByteArray?): Result<Boolean>
    fun getMessage(chatId: String): Flow<Result<List<MessageModel>>>
    suspend fun markAsReadCountAndReadMessages(chatId: String, uid: String): Result<Unit>
    fun markIsReadAndUnreadCountResetLive(chatId: String, uid: String)
    fun stopIsReadLive(chatId: String)

    suspend fun deleteMessage(messageId: String , chatId : String): Boolean
    suspend fun deleteChat(chatId : String , participants: List<String>) : Boolean
    suspend fun editMessage(messageId: String, chatId: String, newContent: String): Boolean
    suspend fun saveUserToken(token: String, uid: String)
    suspend fun isPopUpShowed(chatId: String , messageId: String ) : Boolean
    suspend fun popUpRead(chatId: String, messageId: String)
}