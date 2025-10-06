package com.example.agoracallapp.data.agora

import android.content.Context
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas

// Agora SDK ko manage karne ke liye class - ye video/voice calls ke core functionality handle karta hai
class AgoraManager(private val context: Context ,val  appId: String) {
    private var rtcEngine: RtcEngine? = null

    // Agora engine initialize karta hai with required settings
    fun initialize( ) {
        try {
            rtcEngine = RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {
                // Call ke different events ko handle karta hai
                override fun onUserJoined(uid: Int, elapsed: Int) {
                    // User joined the channel
                }

                override fun onUserOffline(uid: Int, reason: Int) {
                    // User left the channel
                }

                override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
                    // Successfully joined the channel
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Voice call ke liye channel join karta hai
    fun joinVoiceChannel(channelName: String, token: String?, uid: Int) {
        rtcEngine?.setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_COMMUNICATION)
        rtcEngine?.joinChannel(token, channelName, "", uid)
    }

    // Video call ke liye channel join karta hai
    fun joinVideoChannel(channelName: String, token: String?, uid: Int) {
        rtcEngine?.setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_COMMUNICATION)
        rtcEngine?.enableVideo()
        rtcEngine?.joinChannel(token, channelName, "", uid)
    }

    // Local video view set karta hai
    fun setupLocalVideo(view: VideoCanvas) {
        rtcEngine?.setupLocalVideo(view)
    }

    //for pause The Screen
    fun pauseCall(){
        rtcEngine?.pauseAllChannelMediaRelay()
    }

    //for Switch The Camera
    fun switchCamera(){
        rtcEngine?.switchCamera()
    }

    // Remote video view set karta hai
    fun setupRemoteVideo( view: VideoCanvas) {
        rtcEngine?.setupRemoteVideo(view)
    }

    // Channel leave karta hai
    fun leaveChannel() {
        rtcEngine?.leaveChannel()
    }

    // Microphone mute/unmute karta hai
    fun muteLocalAudioStream(muted: Boolean) {
        rtcEngine?.muteLocalAudioStream(muted)
    }

    // Video on/off karta hai
    fun muteLocalVideoStream(muted: Boolean) {
        rtcEngine?.muteLocalVideoStream(muted)
    }

    // Speaker on/off karta hai
    fun setEnableSpeakerphone(enabled: Boolean) {
        rtcEngine?.setEnableSpeakerphone(enabled)
    }

    // Agora engine destroy karta hai
    fun destroy() {
        RtcEngine.destroy()
    }
}