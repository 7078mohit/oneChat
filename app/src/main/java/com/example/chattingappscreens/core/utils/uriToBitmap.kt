package com.example.chattingappscreens.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun uriToBitmap(context: Context , uri : Uri ) : Bitmap? {
    return context.contentResolver.openInputStream(uri)?.use {  inputStream ->
        BitmapFactory.decodeStream(inputStream)
    }
}