package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.domain.repository.AuthRepository

class LogOutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(uid : String) = authRepository.logOut(uid = uid)
}