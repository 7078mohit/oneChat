package com.example.chattingappscreens.presentation.NavGraph

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chattingappscreens.core.common.OnlineStatusManager
import com.example.chattingappscreens.presentation.Profile.PrivacyPolicyWebView
import com.example.chattingappscreens.presentation.auth.SignIn
import com.example.chattingappscreens.presentation.auth.SignUp
import com.example.chattingappscreens.presentation.auth.WelcomeScreen
import com.example.chattingappscreens.presentation.chatting.ChattingScreen
import com.example.chattingappscreens.presentation.home.searchScreen
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.ChatViewModel
import com.example.chattingappscreens.viewmodel.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import okio.ByteString.Companion.encode
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.net.URL
import java.net.URLEncoder.encode

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    firebaseAuth: FirebaseAuth = koinInject() ,
    authViewModel: AuthViewModel = koinViewModel()
) {

   // val isUserAuthed = authViewModel.isUserAuthed.collectAsState().value

    val sharedViewModel: SharedViewModel = koinViewModel()
    val uidFirebase = firebaseAuth.currentUser?.uid
    val navHostController = rememberNavController()
    val chatViewModel: ChatViewModel = koinViewModel()

    val usersChatsState = chatViewModel.getChatState.collectAsState()
    val usersList = usersChatsState.value.isSuccess
    val notificationState = chatViewModel.notificationState.collectAsState().value

    val userAuthorised = uidFirebase?.isNotEmpty() == true
    val startDestination = if (userAuthorised) {

        val permissionOfPostNotification =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        LaunchedEffect(Unit) {
            when (permissionOfPostNotification.status) {
                is PermissionStatus.Denied -> {
                    permissionOfPostNotification.launchPermissionRequest()
                }

                PermissionStatus.Granted -> Log.d("postNotification_Permission", "granted")
            }
        }

        Route.Home.name
    } else {
        Route.Auth.name
    }

    fun defaultEnter() = scaleIn(initialScale = 0.8f, animationSpec = tween(400)) + fadeIn()
    fun defaultExit() = scaleOut(targetScale = 1.2f, animationSpec = tween(400)) + fadeOut()


    Box(modifier = Modifier.fillMaxSize()) {


        AnimatedNavHost(navController = navHostController, startDestination = startDestination) {
            //Home Graph
            navigation(
                route = Route.Home.name, startDestination = Home.Contact.name
            ){
                composable(
                    route = Home.Contact.name
                ) {
                    HomeRootScreen(modifier = modifier, navHostController = navHostController)
                }
            }

            //AuthGraph
            navigation(
                route = Route.Auth.name, startDestination = Auth.Welcome.name
            ) {
                composable(                                                                             // tween time handle krne ke liye hai animation me mtlb ab 7 ms chalega
                    enterTransition = { defaultEnter() },
                    exitTransition = { defaultExit() },
                    popEnterTransition = { defaultEnter() },
                    popExitTransition = { defaultExit() },
                    route = Auth.Welcome.name
                ) {
                    WelcomeScreen(navHostController = navHostController, modifier = modifier)
                }
                composable(
                    enterTransition = { defaultEnter() },
                    exitTransition = { defaultExit() },
                    popEnterTransition = { defaultEnter() },
                    popExitTransition = { defaultExit() },
                    route = Auth.Signup.name
                ) {
                    SignUp(
                        navHostController = navHostController,
                        modifier = modifier,
                        snackBarHostState
                    )
                }
                composable(
                    enterTransition = { defaultEnter() },
                    exitTransition = { defaultExit() },
                    popEnterTransition = { defaultEnter() },
                    popExitTransition = { defaultExit() },
                    route = Auth.SignIn.name
                ) {
                    SignIn(
                        navHostController = navHostController,
                        modifier = modifier,
                        snackBarHostState
                    )
                }
            }

            //details like chatting and Search
            navigation(
                route = Route.Out.name, startDestination = Out.Chatting.name
            ) {
                composable(
                    enterTransition = { defaultEnter() },
                    exitTransition = { defaultExit() },
                    popEnterTransition = { defaultEnter() },
                    popExitTransition = { defaultExit() },
                    route = "${Out.Chatting.name}/{chatId}/{uid}/{userName}/{profile}",
                    arguments = listOf(
                        navArgument("chatId") { type = NavType.StringType },
                        navArgument("uid") { type = NavType.StringType },
                        navArgument("userName") { type = NavType.StringType },
                        navArgument("profile") { type = NavType.StringType })
                ) { backStackEntry ->
                    val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                    val uid = backStackEntry.arguments?.getString("uid") ?: ""
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    val profile = Uri.decode(backStackEntry.arguments?.getString("profile") ?: "")


                    ChattingScreen(
                        userName = userName,
                        profile = profile,
                        navHostController = navHostController,
                        modifier = modifier,
                        uid = uid,
                        chatId = chatId,
                    )
                }


                composable(
                    enterTransition = { defaultEnter() },
                    exitTransition = { defaultExit() },
                    popEnterTransition = { defaultEnter() },
                    popExitTransition = { defaultExit() }
                    , route = Out.Search.name
                ) {
                    searchScreen(
                        navHostController = navHostController,
                        modifier = modifier,
                        snackBarHostState = snackBarHostState
                    )
                }
                composable(Out.Webview.name) {
                    val url = sharedViewModel.privacyUrl
                    PrivacyPolicyWebView(url)
                }
            }
        }

        LaunchedEffect(usersList) {
            // Always process messages, let showNotification decide
            usersList
                ?.filter { it.unreadCount[uidFirebase ?: ""] ?: 0 >= 1 }
                ?.maxByOrNull { it.lastMessageTime }
                ?.let { model ->
                    // showNotification already checks conditions internally
                    if (OnlineStatusManager.isAppInForeground &&
                        ChatStateManager.currentChatId == null) {
                        chatViewModel.showNotification(model)
                    }
                }
        }

//        LaunchedEffect(usersList, OnlineStatusManager.isAppInForeground, ChatStateManager.currentChatId) {
//           if (!OnlineStatusManager.isAppInForeground) return@LaunchedEffect
//            if (ChatStateManager.currentChatId != null) return@LaunchedEffect
//
//                // ab ye ek hi model pick krega
//            usersList?.filter { mergedModel ->
//                val count = mergedModel.unreadCount[uidFirebase ?: ""] ?: 0
//                count >= 1
//            }?.maxByOrNull { mergedModel ->
//                //agr timeStamp hai to latest choose kro, varna bas koi bhi pehla
//                mergedModel.lastMessageTime ?: 0L
//            }?.let { model ->
//                chatViewModel.showNotification(model)
//            }
//        }

//        usersList?.forEach { mergedModel ->
//            Log.d("notificationpopuplistsize", "${usersList.size}")
//            mergedModel.unreadCount[uidFirebase ?: ""]?.let {
//
//                if (!OnlineStatusManager.isAppInForeground) return
//                if (ChatStateManager.currentChatId == null) {
//                    //               chatViewModel.popupRead(chatId = mergedModel.chatId, messageId = mergedModel.messageId)
//                    if (it >= 1) {             // agr ye zero nhi hai
//                        chatViewModel.showNotification(mergedModel)
//                        Log.d("notificationShowedTrigger", "called fun bro")
//                    } else {
//                        return
//                    }
//                } else {
//                    return
//                }
//
//            }
//        }

        AnimatePopup(
            state = notificationState,
            onTap = {
                val encodeImage = Uri.encode(notificationState?.image)
                navHostController.navigate("${Out.Chatting.name}/${notificationState?.chatId}/${notificationState?.uid}/${notificationState?.name}/${encodeImage}")
            },
            onBack = {
                chatViewModel.resetNotificationState()
            }
        )

        Log.d("notificationSTateintheNavGraph","${notificationState?.chatId}")


    }
}
