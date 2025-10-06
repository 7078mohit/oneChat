//package com.example.chattingappscreens.domain.usecase.Call
//
//import com.example.chattingappscreens.domain.repository.CallRepository
//
//class GetCallModelByIdUseCase (private val callRepository: CallRepository){
//    suspend operator fun invoke(callId : String, currentUserId : String) = callRepository.getCallById(callId = callId , currentUserId = currentUserId)
//}