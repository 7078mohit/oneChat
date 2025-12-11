package com.example.chattingappscreens.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

suspend fun urlToBitmap(url : String) : Bitmap?  = withContext(Dispatchers.IO){

    val connection = URL(url).openConnection() as HttpURLConnection
    try {

       connection.doInput = true
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()

       val inputStream : InputStream = connection.inputStream
       BitmapFactory.decodeStream(inputStream)

   } catch (e: Exception){
       Log.d("changeUrltoBitmap", e.message.toString())
       e.printStackTrace()
       null
   } finally {
       connection.disconnect()
   }
}