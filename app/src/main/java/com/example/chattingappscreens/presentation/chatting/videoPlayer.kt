package com.example.chattingappscreens.presentation.chatting

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.chattingappscreens.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoExoPlayer(  context : Context, uri : Uri  , messageId : String){

    val chatViewModel : ChatViewModel = koinViewModel()
   val exoPlayer = chatViewModel.initVideoPlayer(context , uri , messageId  = messageId)


    AndroidView(
        factory = { PlayerView(context).apply {
           player = exoPlayer

            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

          //  setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT) ye bhi kr skte hai bro layout param ki place pr
        }},
        modifier = Modifier.fillMaxSize()

    )

}