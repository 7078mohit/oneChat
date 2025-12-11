package com.example.chattingappscreens.presentation.NavGraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chattingappscreens.presentation.Profile.EditProfileScreen
import com.example.chattingappscreens.presentation.Profile.HelpScreen
import com.example.chattingappscreens.presentation.home.HomeScreen
import com.example.chattingappscreens.presentation.Profile.ProfileScreen
import com.example.chattingappscreens.presentation.Profile.StorageSettingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeRootScreen(modifier: Modifier , navHostController: NavHostController){

    val homeNavHostController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState()}

    val navBackStackEntry = homeNavHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState ) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when{
                Home.Contact.name == currentDestination  -> HomeScreenTopBar(navHostController = navHostController)
                Home.Profile.name == currentDestination -> SimpleTopBar(title = "Profile" , navHostController = homeNavHostController , isBack = false)
                Home.EditProfile.name == currentDestination -> SimpleTopBar(title =  "Edit Profile" , navHostController = homeNavHostController)
                Home.Help.name == currentDestination -> SimpleTopBar(title = "Help" , navHostController = homeNavHostController )
            }
        }
        ,
        bottomBar = {
            BottomBar(modifier = Modifier, homeNavHostController)
        },
    )
    { innerpadding ->

        AnimatedNavHost(
            navController = homeNavHostController , startDestination = Home.Contact.name , modifier = Modifier.padding(innerpadding)
            ){
            composable(
               enterTransition = { scaleIn( initialScale = 0.8f , animationSpec = tween(400)) +  slideInHorizontally(initialOffsetX ={it}  )  },
               exitTransition = { scaleOut(targetScale = 1.2f , animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it }) },
               popEnterTransition =  { scaleIn( initialScale = 0.8f , animationSpec = tween(400)) +  slideInHorizontally(initialOffsetX ={it}  )} ,
               popExitTransition = { scaleOut(targetScale = 1.2f , animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it }) },
               route = Home.Contact.name){
                HomeScreen(
                    navhostController = navHostController,
                    snackBarHostState= snackBarHostState
                )
           }
            composable(
                enterTransition = { scaleIn( initialScale = 0.8f , animationSpec = tween(400)) +  slideInHorizontally(initialOffsetX ={it}  )  },
                exitTransition = { scaleOut(targetScale = 1.2f , animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it }) },
                popEnterTransition =  { scaleIn( initialScale = 0.8f , animationSpec = tween(400)) +  slideInHorizontally(initialOffsetX ={it}  )} ,
                popExitTransition = { scaleOut(targetScale = 1.2f , animationSpec = tween(400)) + slideOutHorizontally(targetOffsetX = { -it }) },
               route = Home.Profile.name){
                ProfileScreen(navHostController = navHostController , snackBarHostState = snackBarHostState , homeNavHostController = homeNavHostController , modifier = modifier)
           }

            composable(route = Home.EditProfile.name){
                EditProfileScreen(snackbarHostState = snackBarHostState , navHostController = homeNavHostController)
            }

//            composable(route = Home.ChatsSetting.name){
//                ChatsSettingScreen(homeNavHostController)
//            }
//            composable(route = Home.Notification.name){
//                NotificationScreen(homeNavHostController)
//            }
//            composable(route = Home.Storage.name){
//                StorageSettingScreen(homeNavHostController)
//            }
            composable(route = Home.Help.name){
                HelpScreen(homeNavHostController , rootNavHost = navHostController)
            }

        }
    }
}