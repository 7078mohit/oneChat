package com.example.chattingappscreens.presentation.chatting

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.chattingappscreens.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

// now this will run using viewModel ok bro how are you
@Composable
fun MusicExoPlayer(context: Context , uri: Uri , messageId : String ) {


    val chatViewModel : ChatViewModel  =  koinViewModel()
    val exoPlayer = remember {  chatViewModel.initMusicPlayer(context , uri , messageId = messageId)}


    AndroidView(
        factory = { PlayerView(context).apply {
            player = exoPlayer
        }},
        modifier = Modifier.size(120.dp).
                clip(RoundedCornerShape(12.dp))
            .border(width = 0.4.dp , color = MaterialTheme.colorScheme.onSurface ,shape =  RoundedCornerShape(12.dp)))

}