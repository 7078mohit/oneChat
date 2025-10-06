package com.example.chattingappscreens.domain.usecase.Chat

import com.example.chattingappscreens.domain.repository.ChatRepository

class GetFriendByUidUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(uid : String) = chatRepository.getFriendByUid(uid = uid)
}