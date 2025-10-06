package com.example.chattingappscreens.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.provider.Settings.System.DEFAULT_RINGTONE_URI
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agoracallapp.data.agora.AgoraManager
import com.example.chattingappscreens.data.modell.CallModel
import com.example.chattingappscreens.data.modell.CallStatus
import com.example.chattingappscreens.domain.repository.CallRepository
import com.google.firebase.auth.FirebaseAuth
import io.agora.rtc2.video.VideoCanvas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CallViewModel(
    private val callRepository: CallRepository,
    private val firebaseAuth: FirebaseAuth,
    private val agoraManager: AgoraManager,
    private val context : Context
) : ViewModel() {


    val currentUserUid = firebaseAuth.currentUser?.uid
    var ringTonePlayer : MediaPlayer? = null

    init {
        if (currentUserUid?.isNotEmpty() == true) {
           agoraManager.initialize()
        }
    }


    private val _getCallByIdState = MutableStateFlow<CallModel?>(null)
    val getCallByIdState = _getCallByIdState.asStateFlow()

    private val _incomingCallState = MutableStateFlow<CallModel?>(null)
    val incomingCallState = _incomingCallState.asStateFlow()


    fun sendCallInvitation(callModel: CallModel) {
        viewModelScope.launch(Dispatchers.IO) {
           callRepository.sendCallInvite(callModel = callModel)
            }
    }


    fun observeIncomingCall(forUid: String , onIncomingCall:(CallModel?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.observeIncomingCall(forUid = forUid , onCallReceived = { callModel ->
                onIncomingCall(callModel)
            }
           )
        }
    }

    fun getCallById(callId : String ) {
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.getCallById(callId = callId , callModel = {
                _getCallByIdState.value = it
            })

        }
    }

    fun updateCallStartTime(callId: String ){
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateStartAndEndTime(callId = callId , timeMode = "startTime")
        }
    }
    fun updateCallEndTime(callId: String ){
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateStartAndEndTime(callId = callId , timeMode = "endTime")
        }
    }

    fun removeGetCallIdListener(callId: String){
        viewModelScope.launch(Dispatchers.IO){
            callRepository.getCallByIdRemoveListener(callId = callId)
        }
    }

    fun removeIncomingCallListener(forUid: String){
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.observeIncomingCallRemoveListener(forUid = forUid)
        }
    }

    fun busyCallStatus(callId: String){
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateCallStatus(callId = callId , status = CallStatus.BUSY)
        }
    }


    fun acceptCallStatus(callId : String ) {
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateCallStatus(callId = callId , status = CallStatus.ACCEPTED)
        }
    }

    fun rejectCallStatus(callId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateCallStatus(callId = callId , status =  CallStatus.REJECTED)
        }
    }

    fun endCallStatus(callId : String) {
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateCallStatus(callId = callId , status = CallStatus.ENDED)
        }
    }

    fun missedCallStatus(callId : String){
        viewModelScope.launch(Dispatchers.IO) {
            callRepository.updateCallStatus(callId = callId , status = CallStatus.MISSED)
        }
    }

    // Voice call start karta hai
    fun startVoiceCall(channelName: String, token: String?, uid: Int) {
        agoraManager.joinVoiceChannel(channelName, token, uid)
    }

    // Video call start karta hai
    fun startVideoCall(channelName: String, token: String?, uid: Int) {
        agoraManager.joinVideoChannel(channelName, token, uid)
    }

    // Local video setup karta hai
    fun setupLocalVideo(view: VideoCanvas) {
        agoraManager.setupLocalVideo(view)
    }

    // Remote video setup karta hai
    fun setupRemoteVideo( view: VideoCanvas) {
        agoraManager.setupRemoteVideo( view)
    }

    // Call end karta hai
    fun endCall() {
        agoraManager.leaveChannel()
    }

    fun pauseCall(){
        agoraManager.pauseCall()
    }

    // Audio mute/unmute karta hai
    fun muteAudio(muted: Boolean) {
        agoraManager.muteLocalAudioStream(muted)
    }

    // Video on/off karta hai
    fun muteVideo(muted: Boolean) {
        agoraManager.muteLocalVideoStream(muted)
    }

    // Speaker on/off karta hai
    fun enableSpeaker(enabled: Boolean) {
        agoraManager.setEnableSpeakerphone(enabled)
    }

    fun getContext() : Context{
        return context
    }

    fun playRingtone(){
        try {
            if (ringTonePlayer == null) {
                ringTonePlayer = MediaPlayer.create(context, DEFAULT_RINGTONE_URI)
                ringTonePlayer?.isLooping = true
            }
            ringTonePlayer?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun stopRingtonePlayer(){
        try {
            ringTonePlayer?.stop()
            ringTonePlayer?.release()
            ringTonePlayer = null
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //When the viewModelIsDestroy
    override fun onCleared(){
        super.onCleared()
        stopRingtonePlayer()
        agoraManager.destroy()
    }



    val agora get() = agoraManager

}


data class SendCallState(
    val success: String? = null,
    val failure: String? = null
)
