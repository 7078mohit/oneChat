package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class StopIsReadLiveUseCase(private val chatRepository: ChatRepository) {
    operator fun invoke(chatId : String) = chatRepository.stopIsReadLive(chatId)
}