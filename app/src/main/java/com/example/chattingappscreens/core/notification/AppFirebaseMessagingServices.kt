package com.example.chattingappscreens.core.notification

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
import kotlin.math.absoluteValue

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

        val notificationId = generateUniqueNotificationId(chatId,message.messageId ?: "")

        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = try {
                urlToBitmap(imageUrl)
            } catch (e: Exception) {
                Log.d("messagingconversion", e.message.toString())
                null
            }

            showNotification(chatId, title, body, bitmap,notificationId)
        }
    }

    private fun generateUniqueNotificationId(chatId: String,messageId : String): Int{
        val chatHash = chatId.hashCode()
        val msgHash = messageId.hashCode()
        return (chatHash xor msgHash ).absoluteValue  // for make unique value
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(chatId: String, title: String, body: String, bitmap: Bitmap?,notificationId: Int) {

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


        notificationManager.notify( notificationId, notification)

        val summaryNotification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("New messages")
            .setContentText("You have new messages in chat")
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(groupKey.hashCode() + 10000, summaryNotification)

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