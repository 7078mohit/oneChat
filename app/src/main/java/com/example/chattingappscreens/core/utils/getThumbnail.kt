package com.example.chattingappscreens.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import java.io.File
fun getThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever().apply {
            setDataSource(context, uri)
        }
        val frame = retriever.frameAtTime
        retriever.release()
        frame
    } catch (e: Exception) {
        Log.e("ThumbnailError", "SecurityException: ${e.message}")
        null
    }
}

