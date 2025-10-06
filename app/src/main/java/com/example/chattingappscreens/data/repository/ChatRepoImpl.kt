package com.example.chattingappscreens.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.chattingappscreens.data.modell.ChatModel
import com.example.chattingappscreens.data.modell.MergedModel
import com.example.chattingappscreens.data.modell.MessageModel
import com.example.chattingappscreens.data.modell.MessageType
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.ChatRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.collections.forEach

class ChatRepoImpl(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : ChatRepository {

    override suspend fun getChat(userId: String): Flow<Result<List<MergedModel>>> = callbackFlow {
        val listener = firebaseDatabase.getReference("User_chats").child(userId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    launch {
                        try {

                            val chatModelList = snapshot.children.mapNotNull { snap ->
                                snap.getValue(ChatModel::class.java)
                            }

                            val mergedModelList = withContext(Dispatchers.IO) {
                                chatModelList.mapNotNull { singleChatModel ->
                                    val friendUid =
                                        singleChatModel.participants.firstOrNull { it != userId }
                                    friendUid?.let { fUid ->
                                        val friendModel =
                                            getFriendById(fUid)                 //async function h to wait krega

                                        friendModel?.let {
                                            getMergedModel(
                                                friendModel = friendModel,
                                                singleChatModel
                                            )
                                        }
                                    }
                                }.sortedByDescending { it.lastMessageTime }
                            }

                            trySend(Result.success(mergedModelList))
                        } catch (e: Exception) {
                            trySend(Result.failure(e))
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Result.failure(error.toException()))
                }
            }
            )
        awaitClose {
            firebaseDatabase.getReference("User_chats").child(userId).removeEventListener(listener)
        }
    }

    private suspend fun getFriendById(uid: String): UserModel? {                       //async fun because use await
        try {
            val task = firebaseDatabase.getReference("User").child(uid).get().await()
            val friendModel = task.value as Map<String, Any>
            return UserModel.mapToUser(friendModel)
        } catch (e: Exception) {
            return null
        }
    }

    private fun getMergedModel(friendModel: UserModel, chatModel: ChatModel): MergedModel {
        return MergedModel(
            uid = friendModel.uid,
            name = friendModel.name,
            phone = friendModel.phone,
            email = friendModel.email,
            profile = friendModel.profile,
            chatId = chatModel.chatId,
            lastMessage = chatModel.lastMessage,
            lastMessageTime = chatModel.lastMessageTime,
            lastMessageSenderId = chatModel.lastMessageSenderId,
            unreadCount = chatModel.unreadCount,
            isGroup = chatModel.isGroup,
            groupName = chatModel.groupName,
            groupImageUrl = chatModel.groupImageUrl,
        )
    }


    override suspend fun createChat(
        participants: List<String>, isGroup: Boolean, groupName: String
    ): Result<String> {

        return try {

            val chatId = firebaseDatabase.getReference("chats").push().key ?: ""
            val time = System.currentTimeMillis()

            // ye bnaya h kyonki  firebase isko  skip krra tha because ye default emptymap tha
            val defaultUnreadCount = participants.associateWith { 0L }

            val chat = ChatModel(
                chatId = chatId,
                participants = participants,
                isGroup = isGroup,
                groupName = groupName,
                createdBy = participants.firstOrNull() ?: "",
                createdAt = time,
                unreadCount =  defaultUnreadCount
            )

            firebaseDatabase.getReference("chats").child(chatId).setValue(chat.toMap()).await()

            //for bothUser Creation
            participants.forEach { userId ->
                firebaseDatabase.getReference("User_chats").child(userId).child(chatId)
                    .setValue(chat.toMap()).await()
            }

            Result.success(chatId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFriendByUid(uid: String): Result<UserModel?> {
        return try {
            val result = firebaseDatabase.getReference("User").child(uid).get().await()
            val userModel = result.value as Map<String, Any>
            Result.success(UserModel.mapToUser(userModel))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMessage(chatId: String): Flow<Result<List<MessageModel>>> = callbackFlow {

        Log.d("chatidiii", chatId)

        val listener = firebaseDatabase.getReference("messages").child(chatId)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val messageList = snapshot.children.mapNotNull { dataSnapshot ->
                        val map = dataSnapshot.value as Map<String, Any>
                        Log.d("mapdatamessage", MessageModel.mapToMessage(map).content)
                        MessageModel.mapToMessage(map)
                    }
                    trySend(Result.success(messageList))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("errorrrr", error.message)
                    trySendBlocking(Result.failure(error.toException()))
                }

            })
        awaitClose {
            firebaseDatabase.getReference("message").child(chatId).removeEventListener(listener)
        }
    }

    private suspend fun uploadMediaUri(uri: String): String? {
        return try {
            //compress krenge bad me bro ok
            val path = "mediaFile_${System.currentTimeMillis()}_.jpg"
            val uploadRef = firebaseStorage.getReference("MediaData").child(path)
            uploadRef.putFile(uri.toUri()).await()
            val downloadUrl = uploadRef.downloadUrl.await()

            downloadUrl.toString()
        } catch (e: Exception) {
            null
        }
    }


    override suspend fun sendMessage(message: MessageModel , thumbnail : ByteArray?): Result<Boolean> {
        return try {

            //storage me upload ki hui image
            val mediaUrl = uploadMediaUri(message.mediaUrl)

            var mediaThumbnailUrl : String? =null
            thumbnail?.let {
                 mediaThumbnailUrl = uploadThumbnail(it)
            }

            val messageId =
                firebaseDatabase.getReference("messages").child(message.chatId).push().key ?: ""
            val finalMessage = message.copy(messageId = messageId, mediaUrl = mediaUrl ?: "" , mediaThumbnailUrl = mediaThumbnailUrl ?: "")

            // message add in thee message Node
            firebaseDatabase
                .getReference("messages")
                .child(message.chatId)
                .child(messageId)
                .setValue(finalMessage.toMap())
                .await()


            Log.d("chatidd", message.chatId)

            updateLastMessageForOuterChats(
                chatId = message.chatId,
                senderId = message.senderId,
                lastMessage = message.content,
                timeStamp = message.timeStamp,
                messageType = message.messageType
            )

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // update lastMessage in the OuterConnection Chats and pass the unread counts to friend
    private suspend fun updateLastMessageForOuterChats(
        chatId: String,
        senderId: String,
        lastMessage: String,
        timeStamp: Long = System.currentTimeMillis(),
        messageType: MessageType
    ) {

        Log.d("chatiddd", " $chatId , $senderId , $lastMessage")

        try {

            val lastMessageFiltered = when (messageType) {
                MessageType.IMAGE -> {
                    "Image.."
                }

                MessageType.AUDIO -> {
                    "Audio.. "
                }

                MessageType.VIDEO -> {
                    "Video.."
                }

                MessageType.DOCUMENT -> {
                    "Document.."
                }

                MessageType.TEXT -> {
                    lastMessage
                }

                MessageType.EMOJI -> TODO()
                MessageType.CONTACT -> TODO()
                MessageType.LOCATION -> TODO()
            }


            //get participantsUid  from chats rootNode
            val chatSnapshot = firebaseDatabase.getReference("chats").child(chatId).get().await()
            val map = chatSnapshot.value as Map<String, Any>
            val participantsUid = ChatModel.mapToChat(map).participants

            val unreadCount =
                ChatModel.mapToChat(map).unreadCount.toMutableMap()       // ye inchats ke hi liye hai


            participantsUid.forEach { participantsId ->
                if (senderId == participantsId) {
                    unreadCount[participantsId] = 0L
                } else {
                    unreadCount[participantsId] = (unreadCount[participantsId] ?: 0L) + 1L
                }
            }

            val update = mapOf(
                "lastMessage" to lastMessageFiltered,
                "lastMessageTime" to timeStamp,
                "lastMessageSenderId" to senderId,
                "unreadCount" to unreadCount
            )


            firebaseDatabase.getReference("chats").child(chatId).updateChildren(update).await()

            // update in the root Node of the Chats


            withContext(Dispatchers.IO) {
                //update in the UserChats forBoth participants
                participantsUid.map { participantsId ->
                    Log.d("paticipantId", participantsId)
                    firebaseDatabase.getReference("User_chats")
                        .child(participantsId)
                        .child(chatId)
                        .updateChildren(update)
                        .await()
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    //for clear unreadMessages and make isReadTrue for these messages when i am offline ok
    override suspend fun markAsReadCountAndReadMessages(chatId: String, uid: String): Result<Unit> {

        return try {
            //get my chat
            val snapshot = firebaseDatabase.getReference("chats").child(chatId).get().await()

            //change in map
            val map = snapshot.value as Map<String, Any>

            val chatModel = ChatModel.mapToChat(map)

            //getUnreadCountsMap from my chats
            val unreadCountMap = chatModel.unreadCount.toMutableMap()

            //reset unreadCounts for me
            if ((unreadCountMap[uid] ?: 0L)  > 0L) {
                unreadCountMap[uid] = 0L
            }

            //pass unread count to Parent Chats
            firebaseDatabase.getReference("chats").child(chatId).child("unreadCount")
                .setValue(unreadCountMap).await()


            //pass unread Counts to both participants chats
            firebaseDatabase.getReference("User_chats").child(uid).child(chatId)
                .child("unreadCount").setValue(unreadCountMap).await()

            //get messages of current User
            val messageSnap = firebaseDatabase.getReference("messages").child(chatId).get().await()
            // update isRead message in the messages
            messageSnap.children.forEach { snapshot ->
                val map = snapshot.value as Map<String, Any> ?: return@forEach
                val messageModel = MessageModel.mapToMessage(map)
                if (messageModel.receiverId == uid && !messageModel.isRead) {

                    firebaseDatabase
                        .getReference("messages")
                        .child(chatId)
                        .child(messageModel.messageId)
                        .child("isRead")
                        .setValue(true)
                        .await()
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    var isReadListener : ValueEventListener? =  null
    var unreadCountListener : ValueEventListener? = null


    // this fun for isRead live chatting ok
    override fun  markIsReadAndUnreadCountResetLive(chatId : String , uid : String){

        //create reference
     val isReadRef = firebaseDatabase.getReference("messages").child(chatId)
        val unreadCountRef = firebaseDatabase.getReference("chats").child(chatId)


        unreadCountListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              val map = snapshot.value as? Map<String , Any>   ?: return
                val chatModel = ChatModel.mapToChat(map)
                val participants = chatModel.participants
                val unreadCount =chatModel.unreadCount.toMutableMap()

                 // resetUnread counts
                if((unreadCount[uid] ?: 0L) > 0L){
                    unreadCount[uid] = 0L
                }


                //set in the parent Node
                firebaseDatabase.getReference("chats").child(chatId).child("unreadCount").setValue(unreadCount)

                //set in the both childs Nodes
                participants.forEach {  participantsId ->
                    firebaseDatabase
                        .getReference("User_chats")
                        .child(participantsId)
                        .child(chatId)
                        .child("unreadCount")
                        .setValue(unreadCount)
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        }


        //make listener
              isReadListener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {  snaps ->
                        //get model from map
                      val map =  snaps.value as Map<String, Any> ?: return@forEach
                        val model = MessageModel.mapToMessage(map)

                        //filtering the only unreadMessages
                        if (model.receiverId == uid && !model.isRead){
                            // finally make isRead
                            firebaseDatabase.getReference("messages")
                                .child(chatId)
                                .child(model.messageId)
                                .child("isRead")
                                .setValue(true)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            }

        isReadListener?.let {
           isReadRef.addValueEventListener(it )
        }
        unreadCountListener?.let {
            unreadCountRef.addValueEventListener(it)
        }
    }

    override fun stopIsReadLive(chatId : String){
        val unreadCountRef = firebaseDatabase.getReference("chats").child(chatId)
        val isReadRef = firebaseDatabase.getReference("messages").child(chatId)
        isReadListener?.let {
        isReadRef.removeEventListener(it)
        }

        unreadCountListener?.let {
        unreadCountRef.removeEventListener(it)
        }

    }

    override suspend fun deleteMessage(messageId: String, chatId: String) : Boolean{
       return try {
           firebaseDatabase.getReference("messages").child(chatId).child(messageId).removeValue()
               .await()
            true
       }catch (e: Exception){
           e.printStackTrace()
           false
       }
    }

    override suspend fun editMessage(messageId: String, chatId: String , newContent : String) : Boolean {
      return try {
          val newContentMap = mapOf(
              "content" to newContent,
              "isEdited" to true,
              "editedAt" to System.currentTimeMillis()
          )
          firebaseDatabase.getReference("messages").child(chatId).child(messageId)
              .updateChildren(newContentMap).await()
          true
      }
      catch (e: Exception){
          e.printStackTrace()
          false
      }
    }

    override suspend fun deleteChat(chatId: String , participantsId : List<String>) : Boolean {
        return try {
            firebaseDatabase.getReference("chats").child(chatId).removeValue().await()
            participantsId.forEach {
                firebaseDatabase.getReference("User_chats").child(it).child(chatId).removeValue().await()
            }
            true
            }catch (e: Exception){
                e.printStackTrace()
            false
        }
    }

    private suspend fun uploadThumbnail(byteArray: ByteArray): String? {
       return try {
            val ref = firebaseStorage.getReference("thumbnail_${System.currentTimeMillis()}_jpg")
            ref.putBytes(byteArray).await()
            val url = ref.downloadUrl.await()
            url.toString()
        }
        catch (e : Exception){
            e.printStackTrace()
            null
        }
    }
}


/*
 //    val chatModelList = mutableListOf<ChatModel>()
                        //    val mergedModelList = mutableListOf<MergedModel>()
                            for (modelSnap in snapshot.children) {
                                val chatMap = modelSnap.value as? Map<String, Any> ?: emptyMap()
                                val chatModel = ChatModel.mapToChat(chatMap)
                                if (chatMap.isNotEmpty()) {
                                    chatModelList.add(chatModel)
                                }
                            }
                            chatModelList.forEach { chatModel ->

                                val friendUid = chatModel.participants.firstOrNull { it != userId }
                                friendUid?.let { uidd ->
                                    val friendModel = getFriendById(uidd)
                                    if (friendModel != null && chatModel != null) {
                                        val mergedModel = getMergedModel(
                                            friendModel = friendModel,
                                            chatModel = chatModel
                                        )
                                        mergedModelList.add(mergedModel)
                                    }
                                }
                            }


                            mergedModelList.sortByDescending { it.lastMessageTime }
 */