package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.domain.repository.AuthRepository

class UpdateUserProfileUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(uid : String, imageUri : String) : Result<Boolean>  = authRepository.updateUserProfile(uid = uid
    ,imageUri = imageUri)
}