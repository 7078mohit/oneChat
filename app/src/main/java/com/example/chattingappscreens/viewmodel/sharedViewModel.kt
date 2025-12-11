package com.example.chattingappscreens.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.chattingappscreens.R
import com.example.chattingappscreens.data.modell.CallModel
import org.koin.androidx.compose.koinViewModel

class SharedViewModel( context: Context) : ViewModel() {


    val privacyUrl = context.getString(R.string.privacy_policy)
}