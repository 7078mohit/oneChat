package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignUpWithEmailUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(user: UserModel): Flow<ResultState<String>> {
        return authRepository.signUpWithEmail(user)
    }
}