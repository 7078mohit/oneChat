package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.AuthRepository

class UpdateUserUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(uid : String ,name : String , phoneNo : String) = authRepository.updateUser(uid = uid ,name = name,phoneNo = phoneNo)
}