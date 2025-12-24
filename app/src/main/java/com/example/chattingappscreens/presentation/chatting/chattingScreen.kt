package com.example.chattingappscreens.presentation.chatting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.utils.bitmaptobytearray
import com.example.chattingappscreens.core.utils.dateFormatter
import com.example.chattingappscreens.core.utils.exactTime
import com.example.chattingappscreens.core.utils.formatedTime
import com.example.chattingappscreens.core.utils.getMIMEType
import com.example.chattingappscreens.core.utils.getThumbnail
import com.example.chattingappscreens.core.utils.uriSizeInMb
import com.example.chattingappscreens.data.modell.CallModel
import com.example.chattingappscreens.data.modell.CallStatus
import com.example.chattingappscreens.data.modell.MessageModel
import com.example.chattingappscreens.data.modell.MessageType
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.presentation.NavGraph.ChatStateManager
import com.example.chattingappscreens.presentation.NavGraph.ChattingTopBar
import com.example.chattingappscreens.viewmodel.ChatViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.io.File
import kotlin.toString

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class
)
@Composable
fun ChattingScreen(
    userName: String,
    profile: String,
    navHostController: NavHostController,
    modifier: Modifier,
    uid: String,
    chatId: String,
) {


    DisposableEffect(Unit) {
        ChatStateManager.currentChatId = chatId
        onDispose {
            ChatStateManager.currentChatId = null
        }
    }


    val firebaseAuth: FirebaseAuth = koinInject()
    val chatViewModel: ChatViewModel = koinViewModel()

    val currentUserId = firebaseAuth.currentUser?.uid ?: ""


    val getFriendByIdState by chatViewModel.getFriendByUidState.collectAsStateWithLifecycle()
    val getMessageState by chatViewModel.getMessageState.collectAsStateWithLifecycle()
    val sendMessageState by chatViewModel.sendMessageState.collectAsStateWithLifecycle()
    val deleteMessageState by chatViewModel.deleteMessageState.collectAsStateWithLifecycle()
    val editMessageState by chatViewModel.editMessageState.collectAsStateWithLifecycle()
    val friendModel = getFriendByIdState.isSuccess
    val chatMessages = getMessageState.isSuccess
    var messageState by remember { mutableStateOf<String?>(null) }
    var selectedVisualMedia by remember { mutableStateOf<List<MessageModel>>(emptyList()) }
    val lazyState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var outputVoice by remember { mutableStateOf<Uri?>(null) }
    var recording by remember { mutableStateOf(false) }
    var currentVoiceFile by remember { mutableStateOf<File?>(null) }
    var recordDuration by remember { mutableIntStateOf(0) }
    var compressedImage by remember { mutableStateOf<Uri?>(null) }
    var openCamera by remember { mutableStateOf(false) }
    var selectedMessage by remember { mutableStateOf<MessageModel?>(null) }
    var isEdit by remember { mutableStateOf(false) }
    var listOfEdit = listOf(
        DialogHint(
            label = "Delete",
            icon = Icons.Default.DeleteOutline,
            onClick = {
                chatViewModel.deleteMessage(
                    messageId = selectedMessage!!.messageId,
                    chatId = selectedMessage!!.chatId
                )
            }
        ),
        DialogHint(
            label = "Edit",
            icon = Icons.Default.Edit,
            onClick = {
                if (selectedMessage!!.messageType == MessageType.TEXT) {
                    messageState = selectedMessage!!.content
                    isEdit = false
                } else {
                    Toast.makeText(context, "Media Item can't be Edit.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

    )

    if (selectedMessage != null) {
        if (isEdit) {
            Editdialog(
                onDismiss = {
                    selectedMessage = null
                    isEdit = false
                },
                title = "Chats Edit",
                data = listOfEdit
            )
        }
    }


    LaunchedEffect(chatMessages?.size) {
        if (!chatMessages.isNullOrEmpty()) {
            lazyState.scrollToItem(0)
        }
    }
    LaunchedEffect(sendMessageState.isSuccess == true || sendMessageState.isLoading == true) {
        if (sendMessageState.isSuccess == true) {
            lazyState.animateScrollToItem(0)
        }
        if (sendMessageState.isLoading == true) {
            lazyState.animateScrollToItem(1)
        }
    }


    LaunchedEffect(deleteMessageState.isSuccess == true || editMessageState.isSuccess == true) {
        if (deleteMessageState.isSuccess == true) {
            isEdit = false
            selectedMessage = null
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            chatViewModel.resetDeleteAndEditStates()
            listOfEdit = emptyList()
        }
        if (editMessageState.isSuccess == true) {
            messageState = ""
            selectedMessage = null
            isEdit = false
            Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show()
            chatViewModel.resetDeleteAndEditStates()
            listOfEdit = emptyList()
        }
    }

    DisposableEffect(chatId) {
        chatViewModel.offlineReadMessageAndReadCount(chatId = chatId, uid = currentUserId)
        chatViewModel.onlineReadMessageAndReadCount(chatId = chatId, uid = currentUserId)
        onDispose {
            chatViewModel.stopReadMessageAndReadCountLiveFun(chatId = chatId)
            chatViewModel.releaseExoPlayer()
        }
    }

    LaunchedEffect(uid.isNotEmpty() || chatId.isNotEmpty()) {
        chatViewModel.getFriendById(uid)
        chatViewModel.getMessage(chatId = chatId)
    }

    LaunchedEffect(getMessageState.isError != null || getFriendByIdState.isError != null || sendMessageState.isError != null) {
        if (getMessageState.isError != null) {
            Log.d("getMessageSTate", getMessageState.isError.toString())
            Toast.makeText(context, getMessageState.isError, Toast.LENGTH_LONG).show()
        }
        if (getFriendByIdState.isError != null) {
            Log.d("getFriendByIdState", getFriendByIdState.isError.toString())

            Toast.makeText(context, getFriendByIdState.isError, Toast.LENGTH_LONG).show()
        }
        if (sendMessageState.isError != null) {
            // ye hai bro error today 08/10/2025
            Log.d("sendmessageState", sendMessageState.isError.toString())

            Toast.makeText(context, sendMessageState.isError, Toast.LENGTH_LONG).show()
        }
    }


    LaunchedEffect(recording) {
        if (recording) {
            recordDuration = 0
            var seconds = 0
            while (recording) {
                delay(1000)
                seconds++
                recordDuration = seconds
            }
        }
    }


    fun startRecording(): File? {
        if (recording) return null // already recording

        val voiceFile = File(context.cacheDir, "voiceNote_${System.currentTimeMillis()}.m4a")
        currentVoiceFile = voiceFile

        return try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                //pass in the string only
                setOutputFile(voiceFile.absolutePath)
                // optional recommended params
                //  setAudioEncodingBitRate(128_000)
                // setAudioSamplingRate(44_100)
                prepare()
                start()
            }
            recording = true
            voiceFile

        } catch (e: Exception) {
            e.printStackTrace()
            mediaRecorder?.release()
            mediaRecorder = null
            currentVoiceFile = null
            recording = false
            null
        }
    }

    fun stopRecording(): File? {
        return try {
            mediaRecorder?.let { recorder ->
                try {
                    recorder.stop()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                recorder.release()
            }

            recording = false
            val file = currentVoiceFile
            file?.let { outputVoice = it.toUri() }

            mediaRecorder = null
            currentVoiceFile = null

            file
        } catch (e: Exception) {
            e.printStackTrace()
            mediaRecorder?.release()
            mediaRecorder = null
            currentVoiceFile = null
            recording = false
            null
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            if (recording) {
                stopRecording()
            }
        }
    }


    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)


    val pickVisualMediaLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenMultipleDocuments()) { uris ->

            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )

                    Log.d("PickedURI", "URI: $uri")
                    Log.d(
                        "PersistedPermissions",
                        context.contentResolver.persistedUriPermissions.joinToString()
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }


            val validUris =
                uris.filter { forValidUri ->                                  //check the size of video is small then 100mb
                    val mimeType = getMIMEType(uri = forValidUri, context = context)
                    if (mimeType != null && mimeType.startsWith("video")) {
                        val uriSize = uriSizeInMb(context, forValidUri)
                        if (uriSize.toInt() > 100) {
                            Toast.makeText(context, "Video to large >100Mb", Toast.LENGTH_LONG)
                                .show()
                            return@filter false
                        } else {
                            return@filter true
                        }
                    } else {
                        return@filter true
                    }
                }


            validUris.let {
                //abhi compress nahi krree hai kuch bhi ok

                val message = validUris.mapNotNull { checkedUri ->
                    getMessageFromUri(
                        uri = checkedUri,
                        context = context,
                        chatId = chatId,
                        senderId = currentUserId,
                        friendUid = friendModel?.uid ?: ""
                    )
                }

                selectedVisualMedia = message

            }
        }


    val multipleVisualMediaPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


    val visualMediaPermissionState =
        rememberMultiplePermissionsState(remember { multipleVisualMediaPermission })

    val recordAudioPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                /////
                Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Some Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    if (openCamera) {
        ClickImage(
            onClick = { uri ->
                openCamera = false
                compressedImage = uri
            },
            onCancel = {
                openCamera = it
            }
        )
    } else {

        Scaffold(
            topBar = {
                //top Bar
                ChattingTopBar(
                    profile = profile,
                    name = userName,
                    navHostController = navHostController,
                    friendModel = friendModel ?: UserModel(),
                )
            },
            //added
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
                .exclude(WindowInsets.navigationBars)
                .exclude(WindowInsets.ime)
        )
        { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
//                    .imePadding()
                //verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.Gray)
                                .padding(vertical = 2.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Notification is Disabled",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                painter = painterResource(R.drawable.notificationicon),
                                modifier = Modifier.size(18.dp),
                                contentDescription = "notfication Icon",
                                // tint = Color.Yellow.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Enable",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .clickable(onClick = {
                                        var intent = Intent().apply {
                                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                            putExtra(
                                                Settings.EXTRA_APP_PACKAGE,
                                                context.packageName
                                            )

                                        }
                                        context.startActivity(intent)
                                    })
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(vertical = 2.dp, horizontal = 12.dp)

                            )

                        }
                    }
                }
                when {

                    getMessageState.isLoading == true -> {
                        UserChatShimmer()
                    }

                    chatMessages.isNullOrEmpty() -> {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Start Chatting",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                            // .imeNestedScroll()
                            ,
                            reverseLayout = true,
                            state = lazyState,
                            contentPadding = PaddingValues(vertical = 8.dp)
                            // verticalArrangement = Arrangement.Bottom
                        ) {
                            if (sendMessageState.isLoading == true) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = "Sending...",
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                            items(
                                items = chatMessages ?: emptyList(),
                                key = { it.messageId }
                            ) { messageModel ->
                                MessageCard(
                                    messageModel,
                                    context = context,
                                    firebaseAuth = firebaseAuth,
                                    friendImage = friendModel?.profile ?: "",
                                    isRead = messageModel.isRead,
                                    selectedMessage = { selectedMessage = it },
                                    isEdit = { isEdit = it }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        // .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                ) {

                    ChatInputField(
                        messageState = messageState,
                        onMessageChange = { messageState = it },
                        compressedImage = compressedImage,
                        outputVoice = outputVoice,
                        selectedVisualMedia = selectedVisualMedia,
                        onSend = {
                            coroutineScope.launch {
                                // image add
                                compressedImage?.let {

                                    chatViewModel.sendMessage(
                                        MessageModel(
                                            chatId = chatId,
                                            senderId = currentUserId,
                                            receiverId = friendModel?.uid
                                                ?: "",
                                            messageType = MessageType.IMAGE,
                                            mediaUrl = compressedImage.toString(),
                                        ),
                                        thumbnail = null
                                    )
                                    //content removed
                                    compressedImage = null
                                }

                                //Recorded audio add
                                outputVoice?.let { voice ->

                                    chatViewModel.sendMessage(
                                        MessageModel(
                                            chatId = chatId,
                                            senderId = currentUserId,
                                            receiverId = friendModel?.uid
                                                ?: "",
                                            messageType = MessageType.AUDIO,
                                            mediaUrl = voice.toString(),
                                        ),
                                        thumbnail = null
                                    )
                                    delay(1000)
                                    outputVoice = null
                                }

                                // visual media add
                                if (selectedVisualMedia.isNotEmpty()) {

                                    selectedVisualMedia.forEach { messageModel ->
                                        var videoUri: ByteArray? = null
                                        val thumbnailBitmap =
                                            getThumbnail(
                                                context,
                                                messageModel.mediaUrl.toUri()
                                            )
                                        if (thumbnailBitmap != null) {
                                            videoUri =
                                                bitmaptobytearray(
                                                    thumbnailBitmap
                                                )
                                        }
                                        if (videoUri != null) {
                                            chatViewModel.sendMessage(
                                                messageModel,
                                                thumbnail = videoUri
                                            )
                                        } else {
                                            chatViewModel.sendMessage(
                                                messageModel,
                                                thumbnail = null
                                            )
                                        }
                                    }
                                    selectedVisualMedia = emptyList()
                                }

                                // text message add
                                if (!messageState.isNullOrBlank() && selectedMessage == null) {

                                    chatViewModel.sendMessage(
                                        MessageModel(
                                            chatId = chatId,
                                            senderId = currentUserId,
                                            receiverId = friendModel?.uid
                                                ?: "",
                                            messageType = MessageType.TEXT,
                                            content = messageState ?: ""
                                        ),
                                        thumbnail = null
                                    )
                                    delay(100)
                                    messageState = null

                                }


                                // EditMesssage text
                                if (selectedMessage != null && messageState != null) {
                                    chatViewModel.editMessage(
                                        messageId = selectedMessage!!.messageId,
                                        chatId = selectedMessage!!.chatId,
                                        newContent = messageState!!
                                    )
                                }
                            }
                        },
                        onImageClear = { compressedImage = null },
                        onVoiceClear = { outputVoice = null },
                        onMediaClear = { selectedVisualMedia = emptyList() },
                        onCameraClick = {
                            if (cameraPermissionState.status.isGranted) {
                                openCamera = true
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        },
                        onMicClick = {
                            if (
                                audioPermissionState.status.isGranted
                            ) {
                                startRecording()
                                Toast.makeText(
                                    context,
                                    "Recording Started",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                audioPermissionState.launchPermissionRequest()
                            }
                        },
                        onStopRecording = { stopRecording() },
                        onAttachClick = {
                            if (visualMediaPermissionState.allPermissionsGranted) {
                                pickVisualMediaLauncher.launch(
                                    arrayOf(
                                        "image/*",
                                        "application/*",
                                        "video/*",
                                        "audio/*"
                                    )
                                )

                            } else {
                                visualMediaPermissionState.launchMultiplePermissionRequest()
                            }
                        },
                        recording = recording,
                        recordDuration = recordDuration
                    )


                    //textfield

                }
            }


        }
    }
}

@Composable
fun ChatInputField(
    messageState: String?,
    onMessageChange: (String?) -> Unit,
    compressedImage: Uri?,
    outputVoice: Uri?,
    selectedVisualMedia: List<MessageModel>,
    onSend: () -> Unit,
    onImageClear: () -> Unit,
    onVoiceClear: () -> Unit,
    onMediaClear: () -> Unit,
    onCameraClick: () -> Unit,
    onMicClick: () -> Unit,
    onStopRecording: () -> Unit,
    onAttachClick: () -> Unit,
    recording: Boolean,
    recordDuration: Int
) {

    OutlinedTextField(
        maxLines = 1,
        keyboardActions = KeyboardActions(
            onSend = {

            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Unspecified,
            showKeyboardOnFocus = true,
            autoCorrectEnabled = true
        ),
        value = messageState ?: "",
        onValueChange = onMessageChange,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .border(
                0.7.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(34.dp)
            ),
        shape = RoundedCornerShape(34.dp),
        placeholder = {
            Text(
                text = "Write your message",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
        ),

        leadingIcon = {
            Box(modifier = Modifier.padding(start = 8.dp)) {

                when {
                    compressedImage != null -> {
                        Box(
                            modifier = Modifier
                                .size(width = 70.dp, height = 80.dp)
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            IconButton(
                                onClick = onImageClear
//                            {
//                            compressedImage = null
//                        }
                                ,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "clear "
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .align(Alignment.BottomStart)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        compressedImage
                                    ),
                                    contentDescription = "show in the field",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            shape = RoundedCornerShape(
                                                12.dp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            width = 0.4.dp
                                        )
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                    }

                    outputVoice != null -> {
                        Box(
                            modifier = Modifier
                                .size(width = 70.dp, height = 80.dp)
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            IconButton(
                                onClick = onVoiceClear
//                            {
//                            outputVoice = null
//                        }
                                ,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "clear "
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .align(Alignment.BottomStart)
                            ) {

                                Image(
                                    imageVector = Outlined.Audiotrack,
                                    contentDescription = "show in the field",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(
                                            shape = RoundedCornerShape(
                                                12.dp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            width = 0.4.dp
                                        )
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                    }

                    selectedVisualMedia.isEmpty() -> {
                        IconButton(
                            onClick = onAttachClick
                        ) {
                            Icon(
                                Icons.Default.AttachFile,
                                contentDescription = "file"
                            )
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .size(width = 70.dp, height = 80.dp)
                                .padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                )
                        ) {
                            IconButton(
                                onClick = onMediaClear

                                ,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(22.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "clear "
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .align(Alignment.BottomStart)
                            ) {
                                Image(
                                    imageVector = Outlined.UploadFile,
                                    contentDescription = "show in the field",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(
                                            shape = RoundedCornerShape(
                                                12.dp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            width = 0.4.dp
                                        )
                                        .align(Alignment.Center),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                    }
                }
            }
        },
        trailingIcon = {
            Box(modifier = Modifier.padding(end = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (messageState.isNullOrBlank() && compressedImage == null && selectedVisualMedia.isEmpty() && outputVoice == null) {

                        //CameraButton
                        IconButton(
                            onClick = onCameraClick
                        ) {
                            Icon(
                                Outlined.CameraAlt,
                                contentDescription = "camera"
                            )
                        }

                        //Recording Button
                        if (recording) {
                            IconButton(
                                onClick = onStopRecording

                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.StopCircle,
                                        contentDescription = "Stop"
                                    )
                                    if (recording) {
                                        Text(
                                            text = "${recordDuration}s",
                                            fontSize = 10.sp,
                                            color = Color.Red
                                        )
                                    }
                                }
                            }
                        } else {
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                onClick = onMicClick

                            )
                            {
                                Icon(
                                    Outlined.Mic,
                                    contentDescription = "Mic"
                                )
                            }
                        }
                    } else {
                        //Send Button
                        IconButton(
                            onClick = onSend

                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "sendIcon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    }

                }
            }
        }
    )

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageCard(
    message: MessageModel,
    context: Context,
    firebaseAuth: FirebaseAuth,
    friendImage: String,
    isRead: Boolean,
    selectedMessage: (MessageModel) -> Unit,
    isEdit: (Boolean) -> Unit
) {

    var selectedImageONClICK by remember { mutableStateOf<Uri?>(null) }
    var onClickVideoUri by remember { mutableStateOf<Uri?>(null) }
    val currentUserUid = firebaseAuth.currentUser?.uid
    val fromMe = message.senderId == currentUserUid


    when (message.messageType) {
        MessageType.TEXT -> {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                selectedMessage(message)
                                isEdit(true)

                            }
                        )
                    },
                horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
            ) {

                if (!fromMe) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OtherUserImage(friendImage)

                        ChatBubble(false, message.content, message.timeStamp)
                    }

                } else {
                    ChatBubble(true, message.content, message.timeStamp, isRead = isRead)
                }

            }

        }

        MessageType.IMAGE -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
            ) {
                if (!fromMe) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OtherUserImage(friendImage)

                        GlideImage(
                            model = message.mediaUrl,
                            contentDescription = "clicked Image",
                            modifier = Modifier
                                .heightIn(max = 220.dp)
                                .widthIn(max = 200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = 0.4.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            selectedMessage(message)
                                            isEdit(true)
                                        },
                                        onTap = {
                                            selectedImageONClICK = message.mediaUrl.toUri()
                                        }
                                    )
                                },
                            loading = placeholder {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        trackColor = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
//                                .clickable(onClick = {
//                                })
                            ,
                            contentScale = ContentScale.Crop
                        )

                    }

                } else {
                    GlideImage(
                        model = message.mediaUrl,
                        contentDescription = "clicked Image",
                        modifier = Modifier
                            .heightIn(max = 220.dp)
                            .widthIn(max = 200.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        selectedMessage(message)
                                        isEdit(true)
                                    },
                                    onTap = {
                                        selectedImageONClICK = message.mediaUrl.toUri()
                                    }
                                )
                            },
                        loading = placeholder {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    trackColor = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        contentScale = ContentScale.Crop
                    )
                }


                if (selectedImageONClICK != null) {
                    Dialog(
                        onDismissRequest = { selectedImageONClICK = null },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .clickable(onClick = { selectedImageONClICK = null })
                                .background(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            ZoomableImage(selectedImageONClICK!!)
                        }
                    }
                }
            }

        }

        MessageType.AUDIO -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                selectedMessage(message)
                                isEdit(true)
                            }
                        )
                    },

                horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
            ) {

                if (!fromMe) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        OtherUserImage(friendImage)

                        // left Audio

//                        Box(modifier = Modifier.wrapContentSize()) {
                        MusicExoPlayer(
                            context = context,
                            message.mediaUrl.toUri(),
                            message.messageId
                        )
//                        }
                    }

                } else {
                    //right Audio
//                    Box(modifier = Modifier.wrapContentSize()) {
                    MusicExoPlayer(context = context, message.mediaUrl.toUri(), message.messageId)
//                    }
                }

            }
        }

        MessageType.VIDEO -> {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
            ) {

                if (!fromMe) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        OtherUserImage(friendImage)

                        // left Video

                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                            //   .clickable(onClick = { onClickVideoUri = message.mediaUrl.toUri() })
                        ) {

                            if (message.mediaThumbnailUrl.isNotEmpty()) {

                                GlideImage(
                                    model = message.mediaThumbnailUrl,
                                    contentDescription = "thumbnail",
                                    modifier = Modifier
                                        .heightIn(max = 220.dp)
                                        .widthIn(max = 200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = 0.4.dp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = {
                                                    selectedMessage(message)
                                                    isEdit(true)
                                                },
                                                onTap = {
                                                    onClickVideoUri = message.mediaUrl.toUri()
                                                }
                                            )
                                        },
                                    loading = placeholder {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                trackColor = MaterialTheme.colorScheme.surface,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                )
                                Icon(
                                    Icons.Default.PlayCircle,
                                    contentDescription = "play",
                                    modifier = Modifier
                                        .align(
                                            Alignment.Center
                                        )
                                        .size(50.dp),
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            } else {
                                Text("Loading...", modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }

                } else {
                    //right video
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                        //  .clickable(onClick = { onClickVideoUri = message.mediaUrl.toUri() })
                    ) {
                        if (message.mediaThumbnailUrl.isNotEmpty()) {

                            GlideImage(
                                model = message.mediaThumbnailUrl,
                                contentDescription = "thumbnail",
                                modifier = Modifier
                                    .heightIn(max = 220.dp)
                                    .widthIn(max = 200.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = 0.4.dp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                selectedMessage(message)
                                                isEdit(true)
                                            },
                                            onTap = {
                                                onClickVideoUri = message.mediaUrl.toUri()
                                            }
                                        )
                                    },
                                loading = placeholder {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            trackColor = MaterialTheme.colorScheme.surface,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            )
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = "play",
                                modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                                    .size(50.dp),
                                tint = MaterialTheme.colorScheme.surface
                            )
                        } else {
                            Text("Loading...", modifier = Modifier.align(Alignment.Center))
                        }
                    }

                }

                if (onClickVideoUri != null) {
                    Dialog(
                        onDismissRequest = { onClickVideoUri = null },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {

                            VideoExoPlayer(
                                context = context,
                                uri = onClickVideoUri!!,
                                messageId = message.messageId
                            )

                        }
                    }
                }
            }
        }

        MessageType.DOCUMENT -> {}
        MessageType.EMOJI -> TODO()
        MessageType.CONTACT -> TODO()
        MessageType.LOCATION -> TODO()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ZoomableImage(uri: Uri) {
    var scale by remember { mutableFloatStateOf(1f) }                                //for zoom
    var offset by remember { mutableStateOf(Offset.Zero) }                                    //for move

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        scale *= zoomChange
        offset += panChange
    }
    GlideImage(
        model = uri,
        contentDescription = "clicked image",
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale.coerceIn(1f, 4f),
                scaleY = scale.coerceIn(1f, 4f),
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(
                state = transformableState,
            ),
        contentScale = ContentScale.Crop,
//        loading = placeholder {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator(
//                    trackColor = MaterialTheme.colorScheme.surface,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
    )
}


//only single message like hello only for this
@Composable
fun ChatBubble(fromMe: Boolean, text: String?, timeStamp: Long, isRead: Boolean = false) {
    val bubbleColor =
        if (fromMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor =
        if (fromMe) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier
            .wrapContentSize()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(
                    topStart = if (fromMe) 18.dp else 8.dp,
                    topEnd = if (fromMe) 8.dp else 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 18.dp
                ),
                spotColor = Color.Black.copy(alpha = 0.1f)
            )
//            .clip(
//                RoundedCornerShape(
//                    topStart = if (fromMe) 18.dp else 0.dp,
//                    bottomStart = 18.dp,
//                    topEnd = 18.dp,
//                    bottomEnd = 18.dp
//                )
//            )
            .background(
                color = bubbleColor,
                shape = RoundedCornerShape(
                    topStart = if (fromMe) 18.dp else 8.dp,
                    bottomStart = 18.dp,
                    topEnd = if (fromMe) 8.dp else 18.dp,
                    bottomEnd = 18.dp
                )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        Column {
            Text(
                text = text.orEmpty(),
                modifier = Modifier.padding(bottom = 2.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                color = textColor
            )
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatedTime(timeStamp),
                    fontSize = 8.sp,
                    color = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 4.dp)
                )
                if (fromMe) {
                    Icon(
                        imageVector = if (isRead) Icons.Filled.DoneAll else Icons.Outlined.DoneAll,
                        contentDescription = "read status",
                        modifier = Modifier.size(12.dp),
                        tint = if (isRead) Color(0xFF34B7F1) else textColor.copy(alpha = 0.5f)
                    )
                }
            }
        }


//        Text(
//            text = text.orEmpty(),
//            modifier = Modifier.padding(6.dp),
//            fontWeight = FontWeight.SemiBold,
//            fontSize = 16.sp,
//            style = MaterialTheme.typography.bodyLarge,
//            color = if (fromMe) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
//        )
//
//        if (fromMe) {
//            Icon(
//                imageVector = if (isRead) Icons.Filled.DoneAll else Outlined.DoneAll,
//                contentDescription = "isRead",
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .size(15.dp)
//                    .padding(end = 2.5.dp, bottom = 1.5.dp),
//                tint = if (isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
//            )
//        }
    }
}


fun getMessageFromUri(
    uri: Uri,
    context: Context,
    chatId: String,
    senderId: String,
    friendUid: String
): MessageModel? {
    val mimeType = getMIMEType(context = context, uri = uri) ?: return MessageModel(
        mediaUrl = uri.toString(),
        messageType = MessageType.DOCUMENT
    )


    return when {
        mimeType.startsWith("audio/") || mimeType == "application/octet-stream" -> MessageModel(
            chatId = chatId,
            senderId = senderId,
            receiverId = friendUid,
            mediaUrl = uri.toString(),
            messageType = MessageType.AUDIO
        )


        mimeType.startsWith("video/") -> MessageModel(
            chatId = chatId,
            senderId = senderId,
            receiverId = friendUid ?: "",
            mediaUrl = uri.toString() ?: "",
            messageType = MessageType.VIDEO
        )

        mimeType.startsWith("image/") -> MessageModel(
            chatId = chatId,
            senderId = senderId,
            receiverId = friendUid ?: "",
            mediaUrl = uri.toString() ?: "",
            messageType = MessageType.IMAGE
        )

        else -> {
            MessageModel(
                senderId = senderId,
                chatId = chatId,
                mediaUrl = uri.toString() ?: "",
                messageType = MessageType.DOCUMENT
            )
        }

    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OtherUserImage(friendImage: String) {
    GlideImage(
        model = friendImage,
        contentDescription = "image",
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = CircleShape),
        contentScale = ContentScale.Crop,
        loading = placeholder {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    trackColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
    )

}


//OutlinedTextField(
//maxLines = 1,
//keyboardActions = KeyboardActions(
//onSend = {
//
//}
//),
//keyboardOptions = KeyboardOptions(
//keyboardType = KeyboardType.Text,
//imeAction = ImeAction.Unspecified,
//showKeyboardOnFocus = true,
//autoCorrectEnabled = true
//),
//value = messageState ?: "",
//onValueChange = { messageState = it },
//modifier = Modifier
//.fillMaxWidth()
//.border(
//0.7.dp,
//color = MaterialTheme.colorScheme.primary,
//shape = RoundedCornerShape(34.dp)
//),
//shape = RoundedCornerShape(34.dp),
//placeholder = {
//    Text(
//        text = "Write your message",
//        maxLines = 1,
//        style = MaterialTheme.typography.bodyMedium
//    )
//},
//colors = OutlinedTextFieldDefaults.colors(
//unfocusedBorderColor = Color.Transparent,
//focusedBorderColor = Color.Transparent,
//unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
//unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
//focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
//focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
//focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
//focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
//),
//leadingIcon = {
//    Box(modifier = Modifier.padding(start = 8.dp)) {
//
//        when {
//            compressedImage != null -> {
//                Box(
//                    modifier = Modifier
//                        .size(width = 70.dp, height = 80.dp)
//                        .padding(
//                            horizontal = 8.dp,
//                            vertical = 4.dp
//                        )
//                ) {
//                    IconButton(
//                        onClick = {
//                            compressedImage = null
//                        },
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .size(22.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Rounded.Close,
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.size(20.dp),
//                            contentDescription = "clear "
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .size(42.dp)
//                            .align(Alignment.BottomStart)
//                    ) {
//                        Image(
//                            painter = rememberAsyncImagePainter(
//                                compressedImage
//                            ),
//                            contentDescription = "show in the field",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(RoundedCornerShape(12.dp))
//                                .border(
//                                    shape = RoundedCornerShape(
//                                        12.dp
//                                    ),
//                                    color = MaterialTheme.colorScheme.onSurface,
//                                    width = 0.4.dp
//                                )
//                                .align(Alignment.Center),
//                            contentScale = ContentScale.Crop,
//                        )
//                    }
//                }
//            }
//
//            outputVoice != null -> {
//                Box(
//                    modifier = Modifier
//                        .size(width = 70.dp, height = 80.dp)
//                        .padding(
//                            horizontal = 8.dp,
//                            vertical = 4.dp
//                        )
//                ) {
//                    IconButton(
//                        onClick = {
//                            outputVoice = null
//
//                        },
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .size(22.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Rounded.Close,
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.size(20.dp),
//                            contentDescription = "clear "
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .size(42.dp)
//                            .align(Alignment.BottomStart)
//                    ) {
//
//                        Image(
//                            imageVector = Outlined.Audiotrack,
//                            contentDescription = "show in the field",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(RoundedCornerShape(12.dp))
//                                .background(Color.White)
//                                .border(
//                                    shape = RoundedCornerShape(
//                                        12.dp
//                                    ),
//                                    color = MaterialTheme.colorScheme.onSurface,
//                                    width = 0.4.dp
//                                )
//                                .align(Alignment.Center),
//                            contentScale = ContentScale.Crop,
//                        )
//                    }
//                }
//            }
//
//            selectedVisualMedia.isEmpty() -> {
//                IconButton(onClick = {
//                    if (visualMediaPermissionState.allPermissionsGranted) {
//
//                        pickVisualMediaLauncher.launch(
//                            arrayOf(
//                                "image/*",
//                                "application/*",
//                                "video/*",
//                                "audio/*"
//                            )
//                        )
//
//                    } else {
//                        visualMediaPermissionState.launchMultiplePermissionRequest()
//                    }
//                }
//                ) {
//                    Icon(
//                        Icons.Default.AttachFile,
//                        contentDescription = "file"
//                    )
//                }
//            }
//
//            else -> {
//                Box(
//                    modifier = Modifier
//                        .size(width = 70.dp, height = 80.dp)
//                        .padding(
//                            horizontal = 8.dp,
//                            vertical = 4.dp
//                        )
//                ) {
//                    IconButton(
//                        onClick = {
//                            selectedVisualMedia = emptyList()
//                        },
//                        modifier = Modifier
//                            .align(Alignment.TopEnd)
//                            .size(22.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Rounded.Close,
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.size(20.dp),
//                            contentDescription = "clear "
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .size(42.dp)
//                            .align(Alignment.BottomStart)
//                    ) {
//                        Image(
//                            imageVector = Outlined.UploadFile,
//                            contentDescription = "show in the field",
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(RoundedCornerShape(12.dp))
//                                .background(Color.White)
//                                .border(
//                                    shape = RoundedCornerShape(
//                                        12.dp
//                                    ),
//                                    color = MaterialTheme.colorScheme.onSurface,
//                                    width = 0.4.dp
//                                )
//                                .align(Alignment.Center),
//                            contentScale = ContentScale.Crop,
//                        )
//                    }
//                }
//            }
//        }
//    }
//},
//trailingIcon = {
//    Box(modifier = Modifier.padding(end = 8.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            if (messageState.isNullOrBlank() && compressedImage == null && selectedVisualMedia.isEmpty() && outputVoice == null) {
//
//                //CameraButton
//                IconButton(
//                    onClick = {
//                        if (cameraPermissionState.status.isGranted) {
//                            openCamera = true
//                        } else {
//                            cameraPermissionState.launchPermissionRequest()
//                        }
//                    }
//                ) {
//                    Icon(
//                        Outlined.CameraAlt,
//                        contentDescription = "camera"
//                    )
//                }
//
//
//                //Recording Button
//                if (recording) {
//                    IconButton(onClick = {
//                        stopRecording()
//                        Toast.makeText(
//                            context,
//                            "Stop Recording",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
////
//                    }) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(
//                                Icons.Default.StopCircle,
//                                contentDescription = "Stop"
//                            )
//                            if (recording) {
//                                Text(
//                                    text = "${recordDuration}s",
//                                    fontSize = 10.sp,
//                                    color = Color.Red
//                                )
//                            }
//                        }
//                    }
//                } else {
//                    IconButton(
//                        colors = IconButtonDefaults.iconButtonColors(
//                            containerColor = MaterialTheme.colorScheme.primaryContainer
//                        ),
//                        onClick = {
//                            if (
//                                audioPermissionState.status.isGranted
//                            ) {
//                                startRecording()
//                                Toast.makeText(
//                                    context,
//                                    "Recording Started",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } else {
//                                audioPermissionState.launchPermissionRequest()
//                            }
////
//                        })
//                    {
//                        Icon(
//                            Outlined.Mic,
//                            contentDescription = "Mic"
//                        )
//                    }
//                }
//            } else {
//                //Send Button
//                IconButton(onClick = {
//
//                    coroutineScope.launch {
//                        // image add
//                        compressedImage?.let {
//
//                            chatViewModel.sendMessage(
//                                MessageModel(
//                                    chatId = chatId,
//                                    senderId = currentUserId,
//                                    receiverId = friendModel?.uid
//                                        ?: "",
//                                    messageType = MessageType.IMAGE,
//                                    mediaUrl = compressedImage.toString(),
//                                ),
//                                thumbnail = null
//                            )
//                            //content removed
//                            compressedImage = null
//                        }
//
//                        //Recorded audio add
//                        outputVoice?.let { voice ->
//
//                            chatViewModel.sendMessage(
//                                MessageModel(
//                                    chatId = chatId,
//                                    senderId = currentUserId,
//                                    receiverId = friendModel?.uid
//                                        ?: "",
//                                    messageType = MessageType.AUDIO,
//                                    mediaUrl = voice.toString(),
//                                ),
//                                thumbnail = null
//                            )
//                            delay(1000)
//                            outputVoice = null
//                        }
//
//                        // visual media add
//                        if (selectedVisualMedia.isNotEmpty()) {
//
//                            selectedVisualMedia.forEach { messageModel ->
//                                var videoUri: ByteArray? = null
//                                val thumbnailBitmap =
//                                    getThumbnail(
//                                        context,
//                                        messageModel.mediaUrl.toUri()
//                                    )
//                                if (thumbnailBitmap != null) {
//                                    videoUri =
//                                        bitmaptobytearray(
//                                            thumbnailBitmap
//                                        )
//                                }
//                                if (videoUri != null) {
//                                    chatViewModel.sendMessage(
//                                        messageModel,
//                                        thumbnail = videoUri
//                                    )
//                                } else {
//                                    chatViewModel.sendMessage(
//                                        messageModel,
//                                        thumbnail = null
//                                    )
//                                }
//                            }
//                            selectedVisualMedia = emptyList()
//                        }
//
//                        // text message add
//                        if (!messageState.isNullOrBlank() && selectedMessage == null) {
//
//                            chatViewModel.sendMessage(
//                                MessageModel(
//                                    chatId = chatId,
//                                    senderId = currentUserId,
//                                    receiverId = friendModel?.uid
//                                        ?: "",
//                                    messageType = MessageType.TEXT,
//                                    content = messageState ?: ""
//                                ),
//                                thumbnail = null
//                            )
//                            delay(100)
//                            messageState = null
//
//                        }
//
//
//                        // EditMesssage text
//                        if (selectedMessage != null && messageState != null) {
//                            chatViewModel.editMessage(
//                                messageId = selectedMessage!!.messageId,
//                                chatId = selectedMessage!!.chatId,
//                                newContent = messageState!!
//                            )
//                        }
//
//                    }
//                }) {
//                    Icon(
//                        Icons.Default.Send,
//                        contentDescription = "sendIcon",
//                        tint = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                }
//
//            }
//
//        }
//    }
//}
//)