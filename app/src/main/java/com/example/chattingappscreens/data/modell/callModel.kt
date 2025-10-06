package com.example.chattingappscreens.data.modell

import kotlinx.serialization.Serializable

@Serializable
data class CallModel(
    val callId : String = "",
    val senderId : String = "",
    val receiverId : String = "",
    val friendName : String = "",
    val channelName : String = "",
    val isVideoCall : Boolean = false,
    val status : CallStatus = CallStatus.INITIATED,
    val startTime : Long = 0L,
    val endTime : Long = 0L,
    val duration : Long = System.currentTimeMillis()
) {
//    constructor() : this(
//        callId = "",
//        senderId = "",
//        receiverId = "",
//        streamId = "",
//        timeStamp = 0L,
//        callType = CallType.VOICE,
//        status = CallStatus.INVITED,
//        incoming = true
//    )
//
//    fun toMap() : Map<String, Any>{
//        return  mapOf(
//            "callId" to callId,
//            "senderId" to senderId,
//            "receiverId" to receiverId,
//            "streamId" to streamId,
//            "timeStamp" to timeStamp,
//            "callType" to callType.name,
//            "status" to status.name,
//            "incoming" to incoming
//        )
//    }
//
//
//    companion object{
//        fun mapToCall( map : Map<String , Any>) : CallModel {
//          return CallModel(
//              callId = map["callId"]  as String ?: "",
//              senderId = map["senderId"] as String ?: "",
//              receiverId = map["receiverId"] as String ?: "",
//              streamId = map["streamId"] as String ?: "",
//              timeStamp = map["timeStamp"] as Long ?: System.currentTimeMillis(),
//              callType = (map["callType"] as? String)?.let { CallType.valueOf(it) }  ?: CallType.VOICE,
//              status = (map["status"] as? String)?.let { CallStatus.valueOf(it) } ?:  CallStatus.INVITED,
//              incoming = map["incoming"] as Boolean ?: true
//            )
//        }
//    }
}



enum class CallType{
    VIDEO,
    VOICE
}

enum class CallStatus{
    INITIATED,
    RINGING,
    ACCEPTED,
    BUSY,
    REJECTED,
    ENDED,
    MISSED
}