package com.example.chattingappscreens.presentation.NavGraph

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chattingappscreens.presentation.Profile.PrivacyPolicyWebView
import com.example.chattingappscreens.presentation.auth.SignIn
import com.example.chattingappscreens.presentation.auth.SignUp
import com.example.chattingappscreens.presentation.auth.WelcomeScreen
import com.example.chattingappscreens.presentation.chatting.CallScreen
import com.example.chattingappscreens.presentation.chatting.ChattingScreen
import com.example.chattingappscreens.presentation.chatting.OnIncomingCallScreen
import com.example.chattingappscreens.presentation.chatting.StartCallRingScreen
import com.example.chattingappscreens.presentation.home.searchScreen
import com.example.chattingappscreens.viewmodel.AuthViewModel
import com.example.chattingappscreens.viewmodel.CallViewModel
import com.example.chattingappscreens.viewmodel.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    firebaseAuth: FirebaseAuth = koinInject()
) {

    val sharedViewModel: SharedViewModel = koinViewModel()


    val uidFirebase = firebaseAuth.currentUser?.uid

    Log.d("hhh", uidFirebase.toString())

    val navHostController = rememberNavController()
    val viewModel: AuthViewModel = koinViewModel()
    val callViewModel: CallViewModel = koinViewModel()
    val context = LocalContext.current


    DisposableEffect(uidFirebase) {
        if (uidFirebase?.isNotEmpty() == true){
            callViewModel.observeIncomingCall(forUid = uidFirebase , onIncomingCall = { callModel ->

                callModel?.let {

                    val currentDestination = navHostController.currentDestination?.route
                    val targetRoute = "${Out.Ringing.name}/${callModel.callId}/${callModel.friendName}/${callModel.isVideoCall}"

                    // agr pehle se Ringing hai to mt krna
                    if (currentDestination != targetRoute){
                        navHostController.navigate(targetRoute)
                    }

                }

            })
        }

        onDispose {
            uidFirebase?.let { callViewModel.removeIncomingCallListener(it) }
        }
    }


    val loggedInPreference = viewModel.isLoggedPreference.collectAsStateWithLifecycle().value
    val uidFromPreference = viewModel.uidPreference.collectAsStateWithLifecycle().value

    val userAuthorised = uidFirebase?.isNotEmpty() == true


    val startDestination = if (userAuthorised) {
        Route.Home.name
    } else {
        Route.Auth.name
    }


    AnimatedNavHost(navController = navHostController, startDestination = startDestination) {

        //Home Graph
        navigation(
            route = Route.Home.name,
            startDestination = Home.Contact.name
        ) {

            composable(
                route = Home.Contact.name
            ) {
                HomeRootScreen(modifier = modifier, navHostController = navHostController)
            }
        }


        //AuthGraph
        navigation(
            route = Route.Auth.name,
            startDestination = Auth.Welcome.name
        ) {
            composable(                                                                             // tween time handle krne ke liye hai animation me mtlb ab 7 ms chalega
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                exitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                popExitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                route = Auth.Welcome.name) {
                WelcomeScreen(navHostController = navHostController, modifier = modifier)
            }
            composable(
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                exitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                popExitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                route = Auth.Signup.name) {
                SignUp(
                    navHostController = navHostController,
                    modifier = modifier,
                    snackBarHostState
                )
            }
            composable(
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                exitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                popEnterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + fadeIn()
                },
                popExitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + fadeOut()
                },
                route = Auth.SignIn.name) {
                SignIn(
                    navHostController = navHostController,
                    modifier = modifier,
                    snackBarHostState
                )
            }

        }

        //details like chatting and Search
        navigation(
            route = Route.Out.name,
            startDestination = Out.Chatting.name
        ) {
            composable(
                enterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + slideInHorizontally(initialOffsetX = { it })
                },
                exitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + slideOutHorizontally(targetOffsetX = { -it })
                },
                popEnterTransition = {
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400)
                    ) + slideInHorizontally(initialOffsetX = { it })
                },
                popExitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(400)
                    ) + slideOutHorizontally(targetOffsetX = { -it })
                },
                route = "${Out.Chatting.name}/{chatId}/{uid}",
                arguments = listOf(
                    navArgument("chatId") { type = NavType.StringType },
                    navArgument("uid") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                val uid = backStackEntry.arguments?.getString("uid") ?: ""


                ChattingScreen(
                    navHostController = navHostController,
                    modifier = modifier,
                    uid = uid,
                    chatId = chatId,
                    onVideoCall = {callModel ->
                        callViewModel.getCallById(callId = callModel.callId) // start the listener
                        navHostController.navigate(
                            "${Out.SendRinging.name}/${callModel.callId}/${callModel.friendName}/${callModel.isVideoCall}"
                        )
                    },
                    onVoiceCall = {callModel ->
                        callViewModel.getCallById(callId = callModel.callId) // start the listener
                        navHostController.navigate(
                            "${Out.SendRinging.name}/${callModel.callId}/${callModel.friendName}/${callModel.isVideoCall}"
                        )
                    }
                )
            }

            composable(
                route = "${Out.Call.name}/{callId}/{isVideoCall}",
                arguments = listOf(
                    navArgument("callId"){type = NavType.StringType},
                    navArgument("isVideoCall"){type = NavType.BoolType}
                )
            ){ backStackEntry ->
                val callId = backStackEntry.arguments?.getString("callId") ?: ""
                val isVideoCall = backStackEntry.arguments?.getBoolean("isVideoCall") ?: false
                CallScreen(
                    callId = callId ,
                    isVideoCall = isVideoCall ,
                    onEndCall = {
                        callViewModel.updateCallEndTime(callId = callId)  // end call time
                        callViewModel.endCallStatus(callId = callId) // end call Status
                        navHostController.popBackStack()
                    },
                    modifier = modifier,
                    callViewModel = callViewModel
                )
            }

            composable(
                route = "${Out.Ringing.name}/{callId}/{friendName}/{isVideoCall}",
                arguments = listOf(
                    navArgument("callId"){type = NavType.StringType},
                    navArgument("friendName"){type = NavType.StringType},
                    navArgument("isVideoCall"){type = NavType.BoolType}
                )
                ){ backStackEntry ->
                val callId = backStackEntry.arguments?.getString("callId") ?: ""
                val friendName = backStackEntry.arguments?.getString("friendName") ?: ""
                val isVideoCall = backStackEntry.arguments?.getBoolean("isVideoCall") ?: false

                OnIncomingCallScreen(
                    modifier = modifier,
                    callId = callId,
                    friendName = friendName,
                    isVideoCall = isVideoCall,
                    onAcceptCall = {
                        callViewModel.updateCallStartTime(callId = callId) // start Call Time
                        callViewModel.acceptCallStatus(callId = callId)      // update accept Status
                        navHostController.navigate(
                            "${Out.Call.name}/${callId}/${isVideoCall}"
                        ){
                            launchSingleTop  = true
                            popUpTo(Out.Ringing.name){
                                inclusive = true

                            }
                        }
                    },
                    onRejectCall = {
                        callViewModel.rejectCallStatus(callId = callId)    //update reject Status
                        navHostController.popBackStack()
                    },
                    callViewModel = callViewModel
                )
            }

            composable(
                route = "${Out.SendRinging.name}/{callId}/{friendName}/{isVideoCall}" ,
                arguments = listOf(
                    navArgument("callId"){type = NavType.StringType},
                    navArgument("friendName"){type = NavType.StringType},
                    navArgument("isVideoCall"){type = NavType.BoolType}
                )
            ){ backStackEntry ->
                val callId = backStackEntry.arguments?.getString("callId") ?: ""
                val friendName = backStackEntry.arguments?.getString("friendName") ?: ""
                val isVideoCall = backStackEntry.arguments?.getBoolean("isVideoCall") ?: false

                StartCallRingScreen(
                    callViewModel = callViewModel,
                    callId = callId,
                    onAcceptCall = {
                        callViewModel.updateCallStartTime(callId = callId) // start call time
                        callViewModel.removeGetCallIdListener(callId = callId) //remove listener
                        navHostController.navigate(
                            "${Out.Call.name}/${callId}/${isVideoCall}"
                        ){
                            launchSingleTop = true
                            popUpTo(Out.SendRinging.name){
                                inclusive = true
                            }
                        }
                        // checking for backstack after use
                    },
                    friendName = friendName,
                    onBack = {
                        callViewModel.removeGetCallIdListener(callId = callId)  // remove listener
                        callViewModel.endCallStatus(callId = callId)
                        navHostController.popBackStack()
                    },
                    onBusy = {
                        callViewModel.busyCallStatus(callId = callId)
                        navHostController.popBackStack()
                    },
                    modifier = modifier
                )
            }

            composable(
                popExitTransition = {
                    scaleOut(
                        targetScale = 1.2f,
                        animationSpec = tween(200)
                    ) + slideOutHorizontally(targetOffsetX = { -it })
                },
                route = Out.Search.name
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
    }

