package com.example.chattingappscreens.core.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

fun compressBitmapToFile(context: Context , bitmap: Bitmap) : File {

    val file = File( context.cacheDir , "compressed_image_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG , 80 , outputStream)
    }

    return file
    // me isko yhi se bhi .toUri kr skta hu ok bro
}






/*

for use in the image bitmap

val bitmap = BitmapFactory.decodeFile(compressedFile.path)
Image(
    bitmap = bitmap.asImageBitmap(),
    contentDescription = "Compressed image"
)

 */