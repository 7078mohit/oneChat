package com.example.chattingappscreens.domain.usecase.Home

import com.example.chattingappscreens.domain.repository.HomeRepository

class SearchUserUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(name: String , myUid : String) = homeRepository.searchUsers(name = name , myUid = myUid)
}