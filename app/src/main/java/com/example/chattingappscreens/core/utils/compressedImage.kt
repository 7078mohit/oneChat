package com.example.chattingappscreens.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

// ye fun me mene 3 all utils fun ko combinee krke fun bnaya hai ab ye single fun hi mereko return krega compresed image

var uriToBitmap: Bitmap? = null
var resizedBitmap: Bitmap? = null
var compressedBitmap: File? = null

fun compressedImageWithUri(context : Context , uri : Uri , maxHeight : Int = 1080 , maxWidth : Int = 1080 ) : Uri? {

    uriToBitmap = uriToBitmap(context = context, uri = uri)

    uriToBitmap?.let {
         resizedBitmap = resizeBitmap(bitmap = uriToBitmap!!, maxHeight = maxHeight, maxWidth = maxWidth)
    }
    resizedBitmap?.let {
        compressedBitmap = compressBitmapToFile(context = context, resizedBitmap!!)
    }

    return compressedBitmap?.toUri()

}