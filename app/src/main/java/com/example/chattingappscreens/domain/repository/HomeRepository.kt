package com.example.chattingappscreens.domain.repository

import com.example.chattingappscreens.ResultState
import com.example.chattingappscreens.data.modell.UserModel


interface HomeRepository{
    suspend fun getAllUsers(): ResultState<List<UserModel> >                 // for show in the suggestion  random user like instagram bro
    suspend fun searchUsers(name: String , myUid : String)  : ResultState<List<UserModel>>
    suspend fun getUserById(uid : String) : ResultState<UserModel>
}