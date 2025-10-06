package com.example.chattingappscreens.domain.repository

import com.example.chattingappscreens.data.modell.CallModel
import com.example.chattingappscreens.data.modell.CallStatus

interface CallRepository {
    suspend fun sendCallInvite(callModel: CallModel): Result<Unit>
    fun observeIncomingCall(forUid : String , onCallReceived:(CallModel?) -> Unit)
    suspend fun updateCallStatus(callId : String , status: CallStatus)
    suspend fun getCallById(callId : String , callModel:(CallModel?) -> Unit)
    fun getCallByIdRemoveListener(callId: String)
    fun observeIncomingCallRemoveListener(forUid: String)
    suspend fun updateStartAndEndTime(callId: String , timeMode : String)

}