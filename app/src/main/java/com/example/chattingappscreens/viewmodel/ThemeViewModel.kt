package com.example.chattingappscreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chattingappscreens.core.preference.PreferenceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    val preferenceData: PreferenceData
): ViewModel() {

    val isDarkMode = preferenceData.isDarkMode.stateIn(
        scope = viewModelScope ,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

     fun setDarkMode(){
         viewModelScope.launch(Dispatchers.IO) {
             preferenceData.setThemeMode(isDarkMode = true)
         }
    }

    fun setLightMode(){
        viewModelScope.launch(Dispatchers.IO){
            preferenceData.setThemeMode(isDarkMode = false)
        }
    }

}