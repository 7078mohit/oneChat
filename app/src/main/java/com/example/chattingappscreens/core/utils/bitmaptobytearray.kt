package com.example.chattingappscreens.core.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun bitmaptobytearray(bitmap: Bitmap ) : ByteArray? {
    return try {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG , 80 , baos)
        baos.toByteArray()
    }
    catch (e : Exception){
        e.printStackTrace()
        null
    }
}