package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.domain.repository.AuthRepository

class DeleteUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(uid: String) = authRepository.deleteUser(uid = uid)
}