package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class OfflineMarkUnreadCountAndIsReadUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chatId :String, uid : String) = chatRepository.markAsReadCountAndReadMessages(chatId = chatId , uid = uid)
}