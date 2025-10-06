package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class GetMessageUseCase(private val chatRepository: ChatRepository){
    operator fun invoke(chatId : String) = chatRepository.getMessage(chatId = chatId)
}