//package com.example.chattingappscreens.domain.usecase.Call
//
//import com.example.chattingappscreens.data.modell.CallModel
//import com.example.chattingappscreens.domain.repository.CallRepository
//
//class ObserveIncomingCallUseCase(private val repository: CallRepository ) {
//    suspend operator fun invoke(forUid : String, onCall:(CallModel?)-> Unit) = repository.observeIncomingCall(forUid = forUid , onCall = onCall)
//}