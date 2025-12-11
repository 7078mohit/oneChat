package com.example.chattingappscreens.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.chattingappscreens.core.common.OnlineStatusManager
import com.example.chattingappscreens.data.modell.MergedModel
import com.example.chattingappscreens.data.modell.MessageModel
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.usecase.Chat.AddChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.DeleteChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.DeleteMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.EditMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetChatUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetFriendByUidUseCase
import com.example.chattingappscreens.domain.usecase.Chat.GetMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.IsPopUpShowed
import com.example.chattingappscreens.domain.usecase.Chat.OfflineMarkUnreadCountAndIsReadUseCase
import com.example.chattingappscreens.domain.usecase.Chat.OnlineMarkUnreadCountAndIsReadUseCase
import com.example.chattingappscreens.domain.usecase.Chat.PopUpRead
import com.example.chattingappscreens.domain.usecase.Chat.SaveTokenUseCase
import com.example.chattingappscreens.domain.usecase.Chat.SendMessageUseCase
import com.example.chattingappscreens.domain.usecase.Chat.StopIsReadLiveUseCase
import com.example.chattingappscreens.presentation.NavGraph.ChatStateManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val addChatUseCase: AddChatUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val getFriendByUidUseCase: GetFriendByUidUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val offlineMarkUnreadCountAndIsReadUseCase: OfflineMarkUnreadCountAndIsReadUseCase,
    private val onlineMarkUnreadCountAndIsReadUseCase: OnlineMarkUnreadCountAndIsReadUseCase,
    private val stopIsReadLiveUseCase: StopIsReadLiveUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val isPopUpShowed: IsPopUpShowed,
    private val popUpRead: PopUpRead,
    firebaseAuth: FirebaseAuth
) : ViewModel() {


    private var notificationJob : Job? = null

    private val _currentChatIdInChat = MutableStateFlow("")
    val chatId = _currentChatIdInChat.asStateFlow()

    private val audioPlayer = mutableMapOf<String, ExoPlayer>()

    private val videoPlayer = mutableMapOf<String, ExoPlayer>()

    val uid = firebaseAuth.currentUser?.uid

    private val _getFriendByUidState = MutableStateFlow(GetFriendByUidState())
    val getFriendByUidState = _getFriendByUidState.asStateFlow()

    private val _getChatState = MutableStateFlow(GetChatState())
    val getChatState = _getChatState.asStateFlow()

    private val _addChatState = MutableStateFlow(AddChatState())
    val addChatState = _addChatState.asStateFlow()

    private val _getMessageState = MutableStateFlow((GetMessageState()))
    val getMessageState = _getMessageState.asStateFlow()

    private val _sendMessageState = MutableStateFlow(SendMessageState())
    val sendMessageState = _sendMessageState.asStateFlow()

    private val _editMessageState = MutableStateFlow(EditMessageState())
    val editMessageState = _editMessageState.asStateFlow()

    private val _deleteMessageState = MutableStateFlow(DeleteMessageState())
    val deleteMessageState = _deleteMessageState.asStateFlow()

    private val _deleteChatState = MutableStateFlow(DeleteChatState())
    val deleteChatState = _deleteChatState.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _body = MutableStateFlow("")
    val body = _body.asStateFlow()

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    private val _notification = MutableStateFlow<NotificationState?>(null)
    val notificationState = _notification.asStateFlow()


    //we use condition because if i have one Exoplayer i reuse this dont make new ok , is se application me multiple exoplayer nahi bnenge
    fun initMusicPlayer(context: Context, uri: Uri, messageId: String): ExoPlayer {
        return audioPlayer.getOrPut(messageId) {
            ExoPlayer.Builder(context).build()
                .apply {
                    setMediaItem(MediaItem.fromUri(uri))
                    prepare()
                    playWhenReady = false
                }
        }
    }

    // for videoPlayer
    fun initVideoPlayer(context: Context, uri: Uri, messageId: String): ExoPlayer {
        return videoPlayer.getOrPut(messageId) {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                playWhenReady = true
            }
        }
    }


    //release the both players
    fun releaseExoPlayer() {
        audioPlayer.forEach { it.value.release() }
        audioPlayer.clear()

        // video bhi isme hi release kra lete hai bro ok
        videoPlayer.forEach { it.value.release() }
        videoPlayer.clear()
    }

    fun resetNotificationState() {
        _notification.value = null
    }

    fun showNotification(model: MergedModel) {
      try  {

          notificationJob?.cancel()

            Log.d("showNotificationTestA", model.name)
           notificationJob = viewModelScope.launch(Dispatchers.IO) {
                val isRead = isPopUpShowed(chatId = model.chatId, messageId = model.messageId)
                if (isRead) return@launch
               //Double-check: agr _notification already isi chat ka hai to skip
               val current = _notification.value
               if (current?.chatId == model.chatId && current.messageId == model.messageId ) return@launch

                popUpRead(model.chatId, messageId = model.messageId)
                Log.d("showNotificationTestC", model.name + "popupreadhochukahai bro")

                _notification.value = NotificationState(
                    body = model.lastMessage,
                    title = model.name,
                    image = model.profile,
                    chatId = model.chatId,
                    uid = model.uid,
                    name = model.name,
                    messageId = model.messageId
                )
            }
        }catch (e: Exception){
            Log.d("showNotification",e.localizedMessage ?: "error")
        }

    }


    fun addChat(participants: List<String>, isGroup: Boolean, groupName: String) {
        _addChatState.value = AddChatState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = addChatUseCase(
                participants = participants,
                isGroup = isGroup, groupName = groupName
            )

            if (result.isSuccess) {
                _addChatState.value =
                    AddChatState(isSuccess = result.getOrNull(), isLoading = false, isError = null)
            }
            if (result.isFailure) {
                _addChatState.value =
                    AddChatState(isError = result.exceptionOrNull()?.localizedMessage.toString())
            }
        }
    }

    fun clearAddChat() {
        AddChatState(isLoading = false, isError = null, isSuccess = null)
    }

    init {
        getChat()
    }

    fun getChat() {
        viewModelScope.launch(Dispatchers.IO) {
            _getChatState.value = GetChatState(isLoading = true, isSuccess = null, isError = null)
            getChatUseCase(userId = uid ?: "").collect {
                when {
                    it.isSuccess -> {
                        _getChatState.value =
                            GetChatState(isLoading = false, isSuccess = it.getOrNull())
                    }
                    it.isFailure -> {
                        _getChatState.value = GetChatState(
                            isLoading = false,
                            isSuccess = null,
                            isError = it.exceptionOrNull()?.localizedMessage.toString()
                        )
                    }

                }
            }
        }
    }

    fun sendMessage(messageModel: MessageModel, thumbnail: ByteArray?) {
        _sendMessageState.value = SendMessageState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = sendMessageUseCase(messageModel = messageModel, thumbnail = thumbnail)
            when {
                result.isSuccess -> {
                    _sendMessageState.value = SendMessageState(isSuccess = true, isLoading = false)
                }

                result.isFailure -> {
                    _sendMessageState.value = SendMessageState(
                        isError = result.exceptionOrNull()?.localizedMessage.toString(),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun clearPopUp() {
        _notification.value = null
    }

    fun resetCurrentChatId() {
        _currentChatIdInChat.value = ""
    }

    fun getMessage(chatId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            _currentChatIdInChat.value = chatId

            _getMessageState.value = GetMessageState(isLoading = true)
            getMessageUseCase(chatId = chatId).collect { data ->

                when {
                    data.isSuccess -> {
                        _getMessageState.value =
                            GetMessageState(isLoading = false, isSuccess = data.getOrNull())

//                        val notificationData = data.getOrNull()!!.map { data ->
//
//                            val getUser = getFriendByUidUseCase(data.senderId)
//                            val user = getUser.getOrNull()
//
//                            NotificationState(
//                                title = user?.name ?: "New Message",
//                                body = data.content,
//                                image = user?.profile ?: "",
//                                chatId = data.chatId,
//                                uid = data.senderId
//                            )
//                        }
//
//                        _notification.value = notificationData.lastOrNull()

                        Log.d("messageget", data.getOrNull()?.size.toString())

                    }

                    data.isFailure -> {
                        _getMessageState.value =
                            GetMessageState(
                                isError = data.exceptionOrNull()?.localizedMessage.toString(),
                                isLoading = false
                            )
                        Log.d("meeessageerrror", data.exceptionOrNull().toString())

                    }
                }
            }
        }
    }

    fun getFriendById(uid: String) {
        _getFriendByUidState.value = GetFriendByUidState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = getFriendByUidUseCase(uid = uid)

            when {
                result.isSuccess -> {
                    _getFriendByUidState.value =
                        GetFriendByUidState(isSuccess = result.getOrNull(), isLoading = false)
                }

                result.isFailure -> {
                    _getFriendByUidState.value = GetFriendByUidState(
                        isError = result.exceptionOrNull()?.localizedMessage,
                        isLoading = false
                    )
                }
            }

        }
    }


    // onetime fun
    // ye tb ke liye jab me message dekhu nahi ya me offline hu ok jaise hi open kru chating of one user to uske sab read ho jaye ye one time hai
    fun offlineReadMessageAndReadCount(chatId: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            offlineMarkUnreadCountAndIsReadUseCase(chatId = chatId, uid = uid)
        }
    }

    fun onlineReadMessageAndReadCount(chatId: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            onlineMarkUnreadCountAndIsReadUseCase(chatId = chatId, uid = uid)
        }
    }

    fun stopReadMessageAndReadCountLiveFun(chatId: String) {
        stopIsReadLiveUseCase(chatId = chatId)
    }


    fun editMessage(messageId: String, chatId: String, newContent: String) {
        _editMessageState.value = EditMessageState(isLoading = true, isSuccess = false)
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                editMessageUseCase(chatId = chatId, newContent = newContent, messageId = messageId)

            if (result) {
                _editMessageState.value = EditMessageState(isLoading = false, isSuccess = true)
            }
        }
    }

    fun deleteMessage(messageId: String, chatId: String) {
        _deleteMessageState.value = DeleteMessageState(isLoading = true, isSuccess = false)
        viewModelScope.launch {
            val result = deleteMessageUseCase(messageId = messageId, chatId = chatId)

            if (result) {
                _deleteMessageState.value = DeleteMessageState(isLoading = false, isSuccess = true)
            }
        }
    }


    fun resetDeleteAndEditStates() {
        EditMessageState(isLoading = false, isSuccess = false)
        DeleteMessageState(isLoading = false, isSuccess = false)
    }


    fun deleteChat(chatId: String, participants: List<String>) {
        _deleteChatState.value = DeleteChatState(isLoading = true, isSuccess = false)
        viewModelScope.launch {
            val result = deleteChatUseCase(chatId = chatId, participants = participants)
            if (result) {
                _deleteChatState.value = DeleteChatState(isLoading = false, isSuccess = true)
            }
        }
    }

    //    private fun isPopUpShowed(chatId : String , messageId : String) : Boolean {
//        var result : Boolean ? = false
//       viewModelScope.launch(Dispatchers.IO) {
//        result  = isPopUpShowed(chatId = chatId, messageId = messageId)
//       }
//       return result ?: false
//    }
//
    fun popupRead(chatId: String, messageId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            popUpRead(chatId = chatId, messageId = messageId)
        }
    }

    fun resetDeleteChatState() {
        DeleteChatState(
            isLoading = false,
            isSuccess = false
        )
    }

    fun saveUserToken(token: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveTokenUseCase(token = token, uid = uid)
        }
    }
}


data class DeleteChatState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false
)

data class EditMessageState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false
)

data class DeleteMessageState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = false
)


data class GetFriendByUidState(
    val isLoading: Boolean? = false,
    val isSuccess: UserModel? = null,
    val isError: String? = null
)

data class SendMessageState(
    val isLoading: Boolean? = false,
    val isSuccess: Boolean? = null,
    val isError: String? = null
)

data class GetMessageState(
    val isLoading: Boolean? = false,
    val isSuccess: List<MessageModel>? = null,
    val isError: String? = null
)

data class AddChatState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)

data class GetChatState(
    val isLoading: Boolean = false,
    val isSuccess: List<MergedModel>? = null,
    val isError: String? = null
)

data class NotificationState(
    val body: String,
    val title: String,
    val image: String,
    val chatId: String,
    val uid: String,
    val name: String,
    val messageId: String
)