package com.example.chattingappscreens.core.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun downloadVideoInCache(context: Context, uri: Uri): Uri? = withContext(Dispatchers.IO) {
    try {
        val file = File(context.cacheDir, "video_${System.currentTimeMillis()}.mp4")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { fileOutputStream ->
                inputStream.copyTo(fileOutputStream)
            }
        }

        if (file.exists() && file.length() > 0) {
            file.toUri()
        } else {
            null
        }
    } catch (e: SecurityException) {
        Log.e("DownloadError", "SecurityException: ${e.message}")
        null
    } catch (e: Exception) {
        Log.e("DownloadError", "Exception: ${e.message}")
        null
    }
}
