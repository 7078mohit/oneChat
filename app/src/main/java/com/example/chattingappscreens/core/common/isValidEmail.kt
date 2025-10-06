package com.example.chattingappscreens.core.common

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    //android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}