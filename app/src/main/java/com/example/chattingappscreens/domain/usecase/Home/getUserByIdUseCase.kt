package com.example.chattingappscreens.domain.usecase.Home

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.UserModel
import com.example.chattingappscreens.domain.repository.HomeRepository

class GetUserByIdUseCase (private val homeRepo: HomeRepository) {
    suspend operator fun invoke(uid : String) = homeRepo.getUserById(uid)
}