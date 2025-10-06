package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.domain.repository.AuthRepository

class UpdateOnlineStatusUseCase(private val authRepository: AuthRepository) {
    suspend operator fun  invoke(isOnline : Boolean, uid : String) = authRepository.updateOnlineStatus( isOnline =isOnline , uid = uid)
}