package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.data.modell.MessageModel
import com.example.chattingappscreens.domain.repository.ChatRepository

class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(messageModel: MessageModel , thumbnail : ByteArray?) = chatRepository.sendMessage(message = messageModel , thumbnail = thumbnail)
}