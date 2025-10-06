package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class GetChatUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(userId : String) = chatRepository.getChat(userId = userId)
}