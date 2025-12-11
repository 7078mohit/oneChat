package com.example.chattingappscreens.core.notification

//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Intent
//import android.graphics.Bitmap
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresPermission
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.chattingappscreens.MainActivity
//import com.example.chattingappscreens.R
//import com.example.chattingappscreens.core.common.OnlineStatusManager
//import com.example.chattingappscreens.core.utils.urlToBitmap
//import com.example.chattingappscreens.presentation.NavGraph.ChatStateManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
///**
// * Production-ready FCM service:
// * - Safe channel creation (checks existing channel — avoids silently losing notifications on Oreo)
// * - Handles large icon safely (never sets null)
// * - Uses versioned CHANNEL_ID to let you bump if you previously created a wrong channel
// * - Short-lived foreground fallback on API 26 (Oreo) to improve delivery on strict OEMs
// * - Respects ChatStateManager.currentChatId (trimmed comparison)
// *
// * NOTE: Ensure your manifest contains:
// * - android.permission.POST_NOTIFICATIONS (for Android 13+)
// * - android.permission.FOREGROUND_SERVICE (recommended for starting foreground on Oreo)
// *
// * This file focuses on robustness across Android 8..15.
// */
//
//class AppFirebaseMessagingService : FirebaseMessagingService() {
//
//    companion object {
//        // Version the channel id if you need to change importance after release.
//        const val CHANNEL_ID_MESSAGES = "messages_channel_v1"
//        private const val CHANNEL_NAME_MESSAGES = "Messages"
//        private const val FOREGROUND_FALLBACK_ID = 9988
//        private const val TAG = "AppFCMService"
//    }
//
//    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
//    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        val uid = firebaseAuth.currentUser?.uid ?: return
//        val ref = firebaseDatabase.getReference("User").child(uid)
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                ref.child("fcmToken").setValue(token)
//            } catch (e: Exception) {
//                Log.w(TAG, "Failed to save FCM token: ${e.message}")
//            }
//        }
//    }
//
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        // If app is foreground, skip system notification; UI will handle it.
//        if (OnlineStatusManager.isAppInForeground) return
//
//        val data = message.data
//        val title = data["title"] ?: "New Message"
//        val body = data["body"] ?: data["text"] ?: "You have a message"
//        val chatId = (data["chatId"] ?: "").trim()
//        val imageUrl = data["profile"] ?: ""
//
//        // If user is currently viewing this chat, skip system notification
//        if (chatId.isNotEmpty() && chatId == (ChatStateManager.currentChatId?.trim() ?: "")) {
//            return
//        }
//
//        // Ensure channel exists (id is versioned; bump when you must change importance)
//        createNotificationChannelIfNeeded()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val bitmap = try {
//                if (imageUrl.isNotBlank()) urlToBitmap(imageUrl) else null
//            } catch (e: Exception) {
//                Log.d(TAG, "Image load failed: ${e.message}")
//                null
//            }
//
//            // On strict Oreo devices a short foreground fallback increases chance of delivery.
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
//                startForegroundFallback()
//            }
//
//            try {
//                postNotification(chatId, title, body, bitmap)
//            } catch (e: Exception) {
//                Log.e(TAG, "Notification post failed: ${e.message}", e)
//            }
//        }
//    }
//
//    private fun createNotificationChannelIfNeeded() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            try {
//                val existing = nm.getNotificationChannel(CHANNEL_ID_MESSAGES)
//                if (existing == null) {
//                    val channel = NotificationChannel(
//                        CHANNEL_ID_MESSAGES,
//                        CHANNEL_NAME_MESSAGES,
//                        NotificationManager.IMPORTANCE_HIGH
//                    ).apply {
//                        description = "Notifications for chat messages"
//                        enableVibration(true)
//                        enableLights(true)
//                        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//                    }
//                    nm.createNotificationChannel(channel)
//                }
//                // If existing channel exists, do not recreate or modify its importance (immutable on device)
//            } catch (e: Exception) {
//                Log.w(TAG, "createChannel failed: ${e.message}")
//            }
//        }
//    }
//
//    /**
//     * Short-lived foreground fallback. Starts foreground with a minimal notification and then
//     * immediately allows posting the real notification. This improves behavior on strict OEMs
//     * for API 26 where background execution limits are enforced aggressively.
//     *
//     * Requires FOREGROUND_SERVICE permission in manifest for some OEMs.
//     */
//    @SuppressLint("ForegroundServiceType")
//    private fun startForegroundFallback() {
//        try {
//            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            // Build a minimal notification using the same channel (channel exists because we created it earlier)
//            val fallback = NotificationCompat.Builder(this, CHANNEL_ID_MESSAGES)
//                .setSmallIcon(R.drawable.appicon)
//                .setContentTitle("") // keep minimal
//                .setContentText("")
//                .setAutoCancel(false)
//                .setPriority(NotificationCompat.PRIORITY_LOW)
//                .build()
//
//            // startForeground must be called from a Service. FirebaseMessagingService is a Service so this is allowed.
//            // If it throws on some OEMs, catch and continue.
//            startForeground(FOREGROUND_FALLBACK_ID, fallback)
//
//            // stopForeground(false) after a short moment is handled implicitly after we post the real notification.
//            // We'll call stopForeground(false) after posting (safe to call even if startForeground failed).
//        } catch (e: Exception) {
//            Log.w(TAG, "startForegroundFallback failed: ${e.message}")
//        }
//    }
//
//    private fun stopForegroundFallback() {
//        try {
//            stopForeground(false)
//        } catch (e: Exception) {
//            Log.w(TAG, "stopForegroundFallback failed: ${e.message}")
//        }
//    }
//
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    private fun postNotification(chatId: String, title: String, body: String, bitmap: Bitmap?) {
//        val groupKey = "chat_group_$chatId"
//        val uniqueId = ((System.currentTimeMillis() and Int.MAX_VALUE.toLong()).toInt())
//
//        // PendingIntent to open MainActivity with the chatId
//        val intent = Intent(this, MainActivity::class.java).apply {
//            putExtra("chatId", chatId)
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        }
//
//        val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        } else {
//            PendingIntent.FLAG_UPDATE_CURRENT
//        }
//
//        val pendingIntent = PendingIntent.getActivity(this, chatId.hashCode(), intent, pendingFlags)
//
//        // Build notification safely: only set large icon when bitmap != null.
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID_MESSAGES)
//            .setSmallIcon(R.drawable.appicon)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setGroup(groupKey)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        if (bitmap != null) {
//            // Big picture style for image + clear large icon in big picture to avoid duplication
//            builder.setLargeIcon(bitmap)
//            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
//        } else {
//            // Use BigTextStyle so long text is visible
//            builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
//        }
//
//        // Post child notification
//        NotificationManagerCompat.from(this).notify(uniqueId, builder.build())
//
//        // Post summary after child notifications
//        val summaryId = groupKey.hashCode()
//        val summaryBuilder = NotificationCompat.Builder(this, CHANNEL_ID_MESSAGES)
//            .setSmallIcon(R.drawable.appicon)
//            .setContentTitle("New messages")
//            .setContentText("You have new messages")
//            .setGroup(groupKey)
//            .setGroupSummary(true)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        NotificationManagerCompat.from(this).notify(summaryId, summaryBuilder.build())
//
//        // stop foreground fallback if it was started
//        stopForegroundFallback()
//    }
//
//    override fun onDestroy() {
//        try {
//            stopForegroundFallback()
//        } catch (_: Exception) {
//        }
//        super.onDestroy()
//    }
//}








//package com.example.chattingappscreens.core.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chattingappscreens.MainActivity
import com.example.chattingappscreens.R
import com.example.chattingappscreens.core.common.OnlineStatusManager
import com.example.chattingappscreens.core.utils.urlToBitmap
import com.example.chattingappscreens.presentation.NavGraph.ChatStateManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppFirebaseMessagingServices : FirebaseMessagingService() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }


    override fun onNewToken(token: String) {
        val uid = firebaseAuth.currentUser?.uid ?: ""
        val ref = firebaseDatabase.getReference("User").child(uid)
        CoroutineScope(Dispatchers.IO).launch {
            ref.child("fcmToken").setValue(token)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (OnlineStatusManager.isAppInForeground) return


        Log.d("fcm_Message ", message.messageId.toString())

        val title = message.data["title"] ?: "New Message"
        val body = message.data["body"] ?: message.data["text"] ?: "You have a message"
        val chatId = message.data["chatId"] ?: ""
        val imageUrl = message.data["profile"] ?: ""

        if(ChatStateManager.currentChatId == chatId) return

        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = try {
                urlToBitmap(imageUrl)
            } catch (e: Exception) {
                Log.d("messagingconversion", e.message.toString())
                null
            }

            showNotification(chatId, title, body, bitmap)
        }
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(chatId: String, title: String, body: String, bitmap: Bitmap?) {

        val channelId = "messages_channel"
        val groupKey = "chat_group_$chatId" // group per chat
        createChannel(channelId = channelId)

//        val style = bitmap?.let { NotificationCompat.BigPictureStyle().bigPicture(it) }
//            ?: NotificationCompat.BigTextStyle().bigText(body)

        val notificationManager = NotificationManagerCompat.from(this)

        // intent to open mainactivity on click
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("chatId", chatId)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, chatId.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // individual message notification child
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(title)
            .setContentText(body)
            //.setStyle(style)
            // .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setLargeIcon(bitmap)
            .setAutoCancel(true)
            .setGroup(groupKey)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val uniqueId = System.currentTimeMillis()
        notificationManager.notify(uniqueId.toInt(), notification)

        val summaryNotification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("New messages")
            .setContentText("You have new messages in chat")
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(groupKey.hashCode(), summaryNotification)

    }


    private fun createChannel(channelId: String) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Messages"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}