package com.example.chattingappscreens.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.common.AnimateLottie
import com.example.chattingappscreens.core.utils.dateFormatter
import com.example.chattingappscreens.presentation.NavGraph.Out
import com.example.chattingappscreens.presentation.chatting.DialogHint
import com.example.chattingappscreens.presentation.chatting.Editdialog
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.ChatViewModel
import com.example.chattingappscreens.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navhostController: NavHostController,
    homeNavHostController: NavHostController,
    viewModel: ChatViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
    homeViewModel: HomeViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {


    val uid = authViewModel.getCurrentUserId()
    val context = LocalContext.current

    val deleteChat by viewModel.deleteChatState.collectAsState()

    val chatState by viewModel.getChatState.collectAsStateWithLifecycle()
    val isEdit by remember { mutableStateOf(false) }
    var selectedChatId  by remember{mutableStateOf<String?>(null)}
    var selectedFriendId by remember { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(chatState.isError != null || deleteChat.isSuccess == true) {
        if (chatState.isError != null) {
            snackBarHostState.showSnackbar(
                message = "Error:${chatState.isError}",
                duration = SnackbarDuration.Long
            )
        }

        if (deleteChat.isSuccess == true){
            Toast.makeText(context , "Deleted" , Toast.LENGTH_SHORT).show()
            viewModel.resetDeleteChatState()
          selectedChatId = null
            selectedFriendId = null
        }
    }



    if (selectedChatId != null && selectedFriendId != null){
      val data =  listOf(
          DialogHint(                                    //ye data class h only
            label = "Delete",
            onClick = {viewModel.deleteChat(chatId = selectedChatId!!, participants = selectedFriendId!!)},
            icon = Icons.Default.DeleteOutline
        )
      )

        Editdialog(
            title = "Friends Edit",
            data = data,
            onDismiss = {
                selectedChatId = null
                selectedFriendId = null}
        )
    }



    Column(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = "One Chat",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(height = 36.dp, width = 160.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .clickable(onClick = { navhostController.navigate(Out.Search.name) }),

                    contentAlignment = Alignment.CenterStart

                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically,
                    ) {
                        Text(
                            text = "Search...",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 10.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 8.dp),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                        )

                    }
                }

            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {

            when {
                chatState.isLoading -> {
                    item {
                        AnimateLottie(R.raw.loadinganimationblue, text = "Loading Chats....")
                    }
                }

                chatState.isSuccess?.isNotEmpty() == true -> {
                    items(chatState.isSuccess ?: emptyList()) { mergedModel ->
                        UserCard(
                            onClick = {
//                                navhostController.currentBackStackEntry?.savedStateHandle?.set("chatId" , mergedModel.chatId)
//                                navhostController.currentBackStackEntry?.savedStateHandle?.set("uid", mergedModel.uid)
                                navhostController.navigate("${Out.Chatting.name}/${mergedModel.chatId}/${mergedModel.uid}")
                            },
                            name = mergedModel.name,
                            uid = mergedModel.uid,
                            email = mergedModel.email,
                            image = mergedModel.profile,
                            phone = mergedModel.phone,
                            lastMessageTime = mergedModel.lastMessageTime,
                            lastMessage = mergedModel.lastMessage,
                            lastMessageSenderId = mergedModel.lastMessageSenderId,
                            unreadCount = mergedModel.unreadCount,
                            currentUserId = uid ?: "",
                            chatId = mergedModel.chatId,
                            selectedChatId = {selectedChatId = it},
                            selectedFriendId = {selectedFriendId = it}

                        )
                    }
                }

                else -> {
                    item {
                        AnimateLottie(R.raw.empty, "No chats found...")
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserCard(
    onClick: () -> Unit,
    name: String,
    chatId : String,
    uid: String,
    email: String,
    image: String,
    phone: String,
    lastMessageTime: Long,
    lastMessageSenderId: String,
    lastMessage: String,
    unreadCount: Map<String, Long>,
    currentUserId: String,
    selectedChatId :(String) -> Unit,
    selectedFriendId :(List<String>) -> Unit,

    ) {

    // ye mere unread count nikal gaye
    val currentUserUnreadCount = unreadCount[currentUserId]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        selectedChatId(chatId)
                        selectedFriendId(listOf(currentUserId, uid))
                    },
                    onTap = { onClick.invoke() }
                )
            }

//            .padding(horizontal = 16.dp , vertical =  8.dp),
        ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
              ,
            verticalAlignment = CenterVertically
        ) {

            if (!image.isEmpty()) {
                GlideImage(
                    model = image,
                    contentDescription = "image",
                    loading = placeholder {
                        Image(
                            painter = painterResource(R.drawable.loadingprof),
                            contentScale = ContentScale.Fit,
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.profilefilled),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Spacer(modifier = Modifier.size(14.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.widthIn(100.dp, 200.dp),
                        text = name,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (lastMessageTime != 0L) {
                        Text(
                            text = "last message: ${dateFormatter(lastMessageTime)}",
                            fontSize = 11.sp,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = lastMessage ?: "",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    currentUserUnreadCount?.let { counts ->
                        if (counts > 0L) {
                            Box(
                                modifier = Modifier
                                    .size(height = 22.dp, width = 25.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center

                            ) {
                                Text(
                                    text = if (counts <= 3L) counts.toInt().toString() else "4+",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

            }
        }
    }

}



