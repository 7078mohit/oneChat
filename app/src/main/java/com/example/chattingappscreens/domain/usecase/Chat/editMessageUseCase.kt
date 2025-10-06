package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class EditMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chatId : String, messageId : String, newContent : String)  : Boolean = chatRepository.editMessage(messageId = messageId , chatId = chatId , newContent = newContent)
}