package com.example.chattingappscreens.core.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun uriSizeInMb(context : Context, uri : Uri) : Long {

    var size : Long = 0

    val cursor = context.contentResolver.query(uri , null , null , null , null)

    cursor?.use {

        val sizeColumnIndex = it.getColumnIndex(OpenableColumns.SIZE)
        if (sizeColumnIndex > -1){
            it.moveToFirst()
            size = it.getLong(sizeColumnIndex)
        }
    }

    return  size.div((1024*1024))                                         // Mb me milega ab ok
}