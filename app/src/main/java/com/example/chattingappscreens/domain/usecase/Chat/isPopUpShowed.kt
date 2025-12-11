package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class IsPopUpShowed(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chatId: String, messageId: String) : Boolean = chatRepository.isPopUpShowed(
        chatId = chatId,
        messageId = messageId
    )
}