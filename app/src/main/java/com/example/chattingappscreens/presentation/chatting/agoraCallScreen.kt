package com.example.chattingappscreens.presentation.chatting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.chattingappscreens.data.modell.CallStatus
import com.example.chattingappscreens.viewmodel.CallViewModel
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

// Active call screen - ye call ke dauran UI show karta hai
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallScreen(
    callId: String,
    isVideoCall: Boolean,
    onEndCall: () -> Unit,
    modifier: Modifier,
    callViewModel: CallViewModel = koinViewModel()
) {
    // Call state observe karta hai
    val callState by callViewModel.incomingCallState.collectAsState()

    // Local aur remote video views ke liye variables
    var localView by remember { mutableStateOf<android.view.View?>(null) }
    var remoteView by remember { mutableStateOf<android.view.View?>(null) }

    var timeLive by rememberSaveable { mutableIntStateOf(0) }
    var muteAudio by remember { mutableStateOf(false) }
    var muteVideo by remember { mutableStateOf(false) }
    var isOnSpeaker by remember { mutableStateOf(false) }





    //for CallTime live
    LaunchedEffect(Unit){
        var startTime = 0
        delay(1000)
        startTime++
        timeLive = startTime

    }





    // Lifecycle events handle karta hai
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // Call start karta hai jab screen resume hoti hai
                    if(isVideoCall){
                        callViewModel.startVideoCall(channelName = callState?.channelName ?: "" , token = null , uid = 0)
                    }
                    else{
                        callViewModel.startVoiceCall(channelName = callState?.channelName ?: "" , token = null , uid = 0)
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    // Call pause karta hai jab screen pause hoti hai
                    callViewModel.pauseCall()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    // Call end karta hai jab screen destroy hoti hai
                    callViewModel.endCall()
                    callViewModel.endCallStatus(callId = callId)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Agora engine se video views setup karta hai
    LaunchedEffect(callState) {
        if (isVideoCall) {
            // Local video view create karta hai
            val surfaceView = RtcEngine.CreateRendererView(callViewModel.getContext())
            callViewModel.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
            localView = surfaceView

            // Remote video view create karta hai
            val remoteSurfaceView = RtcEngine.CreateRendererView(callViewModel.getContext())
            callViewModel.setupRemoteVideo(VideoCanvas(remoteSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))
            remoteView = remoteSurfaceView
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isVideoCall) "Video Call" else "Voice Call",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            if (isVideoCall) {
                // Remote video view
                remoteView?.let { view ->
                    AndroidView(
                        factory = { view },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Local video view (small corner view)
                localView?.let { view ->
                    AndroidView(
                        factory = { view },
                        modifier = Modifier
                            .size(120.dp, 160.dp)
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )
                }
            }  else {
                // Voice call ke liye user info show karta hai
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        modifier = Modifier.size(120.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = callState?.friendName ?: "Unknown",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$timeLive sec", // Timer update karna padega
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            // Call controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Mute/Unmute button
                IconButton(
                    onClick = {
                        muteAudio = !muteAudio
                        callViewModel.muteAudio(muteAudio) },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (muteAudio) Color.Red else Color.Gray,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (muteAudio) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // End call button
                IconButton(
                    onClick = onEndCall,      // in this we need to change the Status
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.Red, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Speaker button (for voice calls)
                if (!isVideoCall) {
                    IconButton(
                        onClick = {
                            isOnSpeaker = !isOnSpeaker
                            callViewModel.enableSpeaker(isOnSpeaker) },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (isOnSpeaker) Color.Green else Color.Gray,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isOnSpeaker) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                            contentDescription = "Speaker",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Video on/off button (for video calls)
                if (isVideoCall) {
                    IconButton(
                        onClick = {
                            muteVideo = !muteVideo
                            callViewModel.muteVideo(muteVideo)},
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (muteVideo) Color.Red else Color.Gray,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (muteVideo) Icons.Default.VideocamOff else Icons.Default.Videocam,
                            contentDescription = "Video",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}