//package com.example.chattingappscreens.domain.usecase.Call
//
//import com.example.chattingappscreens.data.modell.CallModel
//import com.example.chattingappscreens.data.modell.CallStatus
//import com.example.chattingappscreens.domain.repository.CallRepository
//
//class UpdateCallStatusUseCase (private val repository: CallRepository){
//    suspend operator fun invoke(callModel: CallModel) = repository.updateCallStatus(callModel = callModel)
//}