package com.example.chattingappscreens.presentation.chatting

import android.widget.Toast
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.chattingappscreens.data.modell.CallModel
import com.example.chattingappscreens.data.modell.CallStatus
import com.example.chattingappscreens.viewmodel.CallViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartCallRingScreen(
    callViewModel: CallViewModel = koinViewModel(),
    callId : String,
    onAcceptCall:()-> Unit,
    friendName : String,
    onBack:() -> Unit ,
    onBusy:() -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current

    // i do this on the navigation
//    LaunchedEffect(callId) {
//        callViewModel.getCallById(callId)
//    }

    val callModelState = callViewModel.getCallByIdState.collectAsState().value

    var startTime by remember { mutableIntStateOf(0) }

    var callStateOfRing  by remember { mutableStateOf("Ringing...") }

    LaunchedEffect(callId) {
        var seconds = 0
        while (seconds < 60) {
            delay(1000)
            seconds++
        }

        startTime = seconds

        if (startTime == 60) {
            callStateOfRing = "Busy"
            Toast.makeText(context, "Busy", Toast.LENGTH_SHORT).show()

            delay(3000)
            onBusy()
        }
    }

    LaunchedEffect(callModelState?.status) {
        callModelState?.let {
            when (it.status) {
                CallStatus.RINGING -> {
                    callStateOfRing = "Ringing..."
                }

                CallStatus.ACCEPTED -> {
                    callStateOfRing = "Accepted"
                    onAcceptCall()
                }

                CallStatus.REJECTED -> {
                    callStateOfRing = "Rejected"
                    Toast.makeText(context,"Rejected" , Toast.LENGTH_SHORT).show()
                }


                else -> {
                    Unit
                }
            }
        }
    }



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
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
                    text = callStateOfRing,                  //ring like states
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


            //rejectCall
            IconButton(
                onClick = {
                    callViewModel.endCallStatus(callId = callId)
                    callViewModel.removeGetCallIdListener(callId = callId)
                    onBack()
                },
                modifier = Modifier
                    .size(64.dp)
                    .background(color = Color.Red, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = "callEnd",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }


        }

    }
}




