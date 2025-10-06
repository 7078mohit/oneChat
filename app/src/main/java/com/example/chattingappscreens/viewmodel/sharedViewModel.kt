package com.example.chattingappscreens.viewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.chattingappscreens.R

class SharedViewModel(context : Context) : ViewModel() {

    val privacyUrl = context.getString(R.string.privacy_policy)
    val appId = "9c293c3e19ec4e969c5ce6ab01fcfeeb"
}