package com.example.mytasksactivity.repository

import com.example.mytasksactivity.api.RetrofitInstance

class UserRepository {

    suspend fun login(email: String, password: String) =
        RetrofitInstance.api.loginUser(email, password)

    suspend fun register(
        fullName: String, email: String, password: String, gender: String
    ) = RetrofitInstance.api.register(fullName, email, password, gender)
}