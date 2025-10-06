package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class DeleteChatUseCase(private val chatRepository: ChatRepository)   {
    suspend operator fun invoke(chatId: String , participants : List<String>) : Boolean = chatRepository.deleteChat(chatId = chatId , participants =  participants)
}