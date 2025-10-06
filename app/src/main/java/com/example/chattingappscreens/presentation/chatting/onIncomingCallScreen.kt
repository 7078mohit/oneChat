package com.example.chattingappscreens.presentation.chatting


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chattingappscreens.viewmodel.CallViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.function.BooleanSupplier

@Composable
fun OnIncomingCallScreen(
    modifier: Modifier,
    callId: String,               //currently Not In Use
    friendName: String,
    isVideoCall: Boolean,
    onAcceptCall: () -> Unit,
    onRejectCall: () -> Unit,
    callViewModel: CallViewModel = koinViewModel()
){

    LaunchedEffect(Unit) {
        callViewModel.playRingtone()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person",
                    tint = Color.White,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Incoming ${if (isVideoCall) "video" else "voice"} call",
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = friendName,
                    Modifier.size(26.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                //rejectCall
                IconButton(
                    onClick = {
                        callViewModel.stopRingtonePlayer()
                        onRejectCall()
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = Color.Red , CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "callEnd",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                //AcceptButton
                IconButton(
                    onClick = {
                    callViewModel.stopRingtonePlayer()
                        onAcceptCall() },
                    modifier = Modifier.size(64.dp)
                        .background(color = Color.Green , CircleShape)
                    ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "AcceptCall",
                        modifier= Modifier.size(32.dp),
                        tint = Color.White
                    )
                }


            }
        }
    }

}