package com.example.chattingappscreens.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    return try {
        val file = File(context.cacheDir, "thumb_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        // Return content:// URI via FileProvider
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
