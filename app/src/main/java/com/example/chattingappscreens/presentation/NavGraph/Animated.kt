package com.example.chattingappscreens.presentation.NavGraph

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun Animated(visible : Boolean, data: @Composable ()-> Unit ) {

    Log.d("Animated" , "$visible in the Animated")

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -200 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -200 }) + fadeOut(),
    ) {
        data() // ye data hai bro jo andar show hoga
    }
}