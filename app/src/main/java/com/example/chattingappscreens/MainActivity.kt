package com.example.chattingappscreens

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.chattingappscreens.presentation.NavGraph.NavGraph
import com.example.chattingappscreens.ui.theme.ChattingAppScreensTheme
import com.example.chattingappscreens.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewMode = koinInject<ThemeViewModel>()
            // get from preference
            val isDarkMode = themeViewMode.isDarkMode.collectAsState().value
            val snackBarHostState = remember { SnackbarHostState() }
            ChattingAppScreensTheme(
                darkTheme = isDarkMode
            ){
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }  ,
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph( modifier = Modifier.padding(innerPadding) , snackBarHostState)
                }
            }
        }
    }
}



//
//
//android:configChanges="orientation|keyboardHidden|screenSize"
//android:screenOrientation="sensor"
//
//


/*
    <?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
<cache-path
    name="images"
    path="."/>
</paths>

 */