package com.example.chattingappscreens.presentation.NavGraph

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.chattingappscreens.viewmodel.NotificationState

@Composable
fun AnimatePopup(
    state: NotificationState?,
    onTap: () -> Unit,
    onBack: () -> Unit
){

    Log.d("AnimatedPopup" , "${state?.name} in the AnimatedPopup")
    Animated(
        visible = state != null,
        data = {

            Notification(
                state = state,
                onTap = onTap,
                onBack = onBack
            )

        }
    )
}

