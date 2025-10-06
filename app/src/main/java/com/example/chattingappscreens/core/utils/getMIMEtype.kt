package com.example.chattingappscreens.core.utils

import android.content.Context
import android.net.Uri


fun getMIMEType(context: Context , uri: Uri) : String? {
     return context.contentResolver.getType(uri)
}