package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class OnlineMarkUnreadCountAndIsReadUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(chatId : String , uid : String) = chatRepository.markIsReadAndUnreadCountResetLive(chatId = chatId , uid = uid)
}