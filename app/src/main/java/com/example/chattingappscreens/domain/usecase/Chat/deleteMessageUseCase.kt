package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class DeleteMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(messageId : String, chatId : String) : Boolean = chatRepository.deleteMessage(messageId = messageId , chatId = chatId)
}