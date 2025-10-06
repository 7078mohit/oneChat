package com.example.chattingappscreens.presentation.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.chattingappscreens.presentation.NavGraph.Home
import com.example.chattingappscreens.presentation.NavGraph.Out
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.ChatViewModel
import com.example.chattingappscreens.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun searchScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    authViewModel: AuthViewModel = koinViewModel(),
    homeviewModel: HomeViewModel = koinViewModel(),
    chatViewModel: ChatViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState
) {

    val searchStateViewModel by homeviewModel.searchUserByName.collectAsState()
    val searchText by homeviewModel.searchText.collectAsState()

    val addChatState by chatViewModel.addChatState.collectAsStateWithLifecycle()

    var isLoading by remember { mutableStateOf(false) }
    isLoading = addChatState.isLoading

    val context = LocalContext.current

    val currentUserId = authViewModel.getCurrentUserId().takeIf { it?.isNotEmpty() == true }


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = LocalFocusManager.current


    LaunchedEffect(searchStateViewModel.isError, addChatState.isError) {
        if (searchStateViewModel.isError != null) {
            snackBarHostState.showSnackbar(
                message = "Error :${searchStateViewModel.isError}",
                duration = SnackbarDuration.Long
            )
        }
        if (addChatState.isError != null) {
            snackBarHostState.showSnackbar(
                message = "Error :${addChatState.isError}",
                duration = SnackbarDuration.Short
            )
            chatViewModel.clearAddChat()
        }
    }

    LaunchedEffect(!addChatState.isSuccess.isNullOrEmpty()) {
        if (!addChatState.isSuccess.isNullOrEmpty()) {
            Toast.makeText(context, "Add friend Successfully", Toast.LENGTH_LONG).show()
            chatViewModel.clearAddChat()
            navHostController.navigate(Home.Contact.name) {
                popUpTo(Out.Search.name){
                    inclusive = true
                }
            }
        }
    }


    if (addChatState.isLoading) {
        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                trackColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(24.dp)
            )
        }
    }


    Column(
        modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            focusRequester.clearFocus(true)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search                    // enter or search icon on the keyboard
                    ),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                    value = searchText,
                    placeholder = { Text(text = "Search...") },
                    onValueChange = {
                        homeviewModel.updateText(text = it)
//                            viewModel.getSearchUser(it)
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(1f),
                    trailingIcon = {
                        IconButton(onClick = {
                            homeviewModel.updateText(text = "")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "clear"
                            )
                        }
                    }

                )
            }
        }
        if (searchStateViewModel.isSuccess != null) {
            val totalItem = searchStateViewModel.isSuccess?.size
            Text(
                text = "${totalItem} result found",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 6.dp),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {

            when {

                searchStateViewModel.isLoading -> {
                    item {
                        AnimateLottie(R.raw.loadinganimationblue, "Search in progress..")
                    }
                }

                searchStateViewModel.isSuccess.isNullOrEmpty() -> {
                    item {
                        AnimateLottie(R.raw.empty, "Search is Empty")
                    }
                }

                else ->

                    items(searchStateViewModel.isSuccess ?: emptyList()) { user ->

                        UserCardSearchScreen(
                            name = user.name,
                            uid = user.uid,
                            email = user.email,
                            image = user.profile,
                            phone = user.phone,
                            lastSeen = user.lastSeen,
                            isOnline = user.isOnline,
                            onClick = {

                                val participantList = listOf(
                                    currentUserId ?: "",
                                    user.uid
                                )

                                chatViewModel.addChat(
                                    participants = participantList,
                                    isGroup = false,
                                    groupName = "",
                                )
                            }
                        )
                    }
            }

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserCardSearchScreen(
    onClick: () -> Unit,
    name: String,
    uid: String,
    email: String,
    image: String,
    phone: String,
    lastSeen: Long,
    isOnline: Boolean,
) {

    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = CenterVertically
        ) {

            if (!image.isEmpty()) {
                GlideImage(
                    model = image,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp , color = MaterialTheme.colorScheme.onSurface , shape = CircleShape),
                    loading = placeholder {
                        Image(
                            painter = painterResource(R.drawable.loadingprof),
                            contentScale = ContentScale.Fit,
                            contentDescription = ""
                        )
                    },
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

            Spacer(modifier = Modifier.width(14.dp))
            Text(
                modifier = Modifier.widthIn(min = 10.dp, max = 120.dp),
                text = name,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))
            ElevatedButton(
                onClick = onClick,
                modifier = Modifier.wrapContentWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Add friend")
            }
        }

    }
}
