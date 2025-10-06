//package com.example.chattingappscreens.presentation.chatting
//
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import com.example.chattingappscreens.data.modell.CallModel
//import com.example.chattingappscreens.data.modell.CallStatus
//import com.example.chattingappscreens.presentation.NavGraph.Out
//import com.example.chattingappscreens.viewmodel.CallViewModel
//import com.example.chattingappscreens.viewmodel.ChatViewModel
//import org.koin.androidx.compose.koinViewModel
//
//@Composable
//fun CallDialogScreen(callViewModel: CallViewModel = koinViewModel(), chatViewModel: ChatViewModel = org.koin.compose.viewmodel.koinViewModel(), currentUserId : String , navController: NavHostController){
//
//    val userStateById = chatViewModel.getFriendByUidState.collectAsState().value
//    val friend = userStateById.isSuccess            // friend liya by id
//
//    val callModel by callViewModel.observeIncomingCallState.collectAsState()
//
//    var callModelState by remember { mutableStateOf<CallModel?>(null) }
//
//
//
//    LaunchedEffect(callModel) {
//        if (callModel?.receiverId == currentUserId && callModel?.status == CallStatus.INVITED ){
//            callModelState = callModel
//        }
//    }
//
//    callModelState?.let {
//
//        // sender ka UserModel ke liye ye kiya
//        chatViewModel.getFriendById(uid = callModelState?.senderId ?: "")
//
//        AlertDialog(
//            onDismissRequest = {callModelState = null },
//            confirmButton = {
//
//                Button(onClick = {
//                    val acceptCallModel = callModelState!!.copy(
//                        status = CallStatus.ACCEPTED
//                    )
//
//                    //  update Status
//                    callViewModel.acceptCallStatus(
//                        callModel = acceptCallModel
//                    )
//
//                    //navigate kr gaye bro ok
//                    navController.navigate("${Out.ZegoCall.name}/${callModel?.callId}/${friend?.name}/${callModel?.receiverId}/${callModel?.callType}")
//
//
//                }, colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSurface
//                )) {
//                    Text("Accept", style = MaterialTheme.typography.labelMedium)
//                }
//            },
//            dismissButton = {
//
//                Button(onClick = {
//                    val cancelCallModel = callModelState!!.copy(
//                        status = CallStatus.REJECTED
//                    )
//
//                    // status Update for reject call
//                    callViewModel.rejectCallStatus(cancelCallModel)
//
//                    callModelState = null
//                },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.error,
//                        contentColor = MaterialTheme.colorScheme.onSurface
//                    )
//                    )
//                {
//                    Text("Dismiss" , style = MaterialTheme.typography.labelMedium )
//                }
//
//            },
//            title = {Text("Incoming ${callModelState?.callType} call" )},
//            text = {Text("by ${friend?.name} \n ${friend?.phone}")}
//        )
//    }
//}