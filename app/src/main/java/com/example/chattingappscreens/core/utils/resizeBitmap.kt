package com.example.chattingappscreens.core.utils

import android.graphics.Bitmap
import androidx.core.graphics.scale

fun resizeBitmap(bitmap: Bitmap , maxHeight : Int , maxWidth : Int) : Bitmap{

    val ratio : Float = minOf(                                          // minof return only one value which is small
        maxWidth.toFloat()/ bitmap.width,
        maxHeight.toFloat()/bitmap.height
    )

    val height = (bitmap.height * ratio).toInt()
    val width = (bitmap.width * ratio).toInt()

    return Bitmap.createScaledBitmap(bitmap , width , height , true)
   // return  bitmap.scale(width = width , height = height , filter = true)     both same work


}