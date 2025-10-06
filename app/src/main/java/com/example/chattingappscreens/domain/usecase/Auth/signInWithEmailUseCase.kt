package com.example.chattingappscreens.domain.usecase.Auth

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInWithEmailUseCase(private val authRepository: AuthRepository) {
     operator fun  invoke(email : String , password : String) : Flow<ResultState<String>> {
       return authRepository.signInWithEmail(email , password)
    }
}