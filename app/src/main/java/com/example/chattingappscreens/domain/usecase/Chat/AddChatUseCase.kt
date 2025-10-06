package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class AddChatUseCase (private val chatRepository: ChatRepository) {
    suspend operator  fun invoke(participants : List<String>, isGroup : Boolean, groupName : String) = chatRepository.createChat(participants = participants , isGroup = isGroup , groupName = groupName)
}