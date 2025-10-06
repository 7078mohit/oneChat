package com.example.chattingappscreens.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

class IntentUtils {


    companion object {
        fun sendEmail(context: Context, text: String) {
            val intentEmail = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"                               //jin jin app me ye register hoga vo show honge
              //   data = Uri.parse("mailto:7078mohit@gmail.com")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("7078mohit@gmail.com"))
               putExtra(Intent.EXTRA_TEXT, "Hello ,I need help :$text.")
                putExtra(Intent.EXTRA_SUBJECT, "App Support")
            }

            if (intentEmail.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intentEmail, "Send Email"))
            }
        }
    }
}