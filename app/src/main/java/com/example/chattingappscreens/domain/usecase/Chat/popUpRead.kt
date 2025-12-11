package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class PopUpRead (private val chatRepository: ChatRepository){
    suspend operator fun  invoke(chatId : String , messageId : String) = chatRepository.popUpRead(chatId = chatId , messageId = messageId)
}