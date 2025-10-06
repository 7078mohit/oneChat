package com.example.chattingappscreens.domain.usecase.Home

import com.example.chattingappscreens.domain.repository.HomeRepository

class GetAllUsersUserCase ( private val homeRepository: HomeRepository) {
    suspend operator fun invoke() = homeRepository.getAllUsers()
}