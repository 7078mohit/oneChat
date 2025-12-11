package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class SaveTokenUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(token : String, uid : String) = chatRepository.saveUserToken(token,uid)
}