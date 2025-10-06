package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SigningWithGoogleUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(idToken : String) : Flow<ResultState<String>> {
        return authRepository.loginWithGoogle(idToken = idToken)
    }

}