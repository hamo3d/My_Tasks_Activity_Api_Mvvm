package com.example.mytasksactivity.model

data class User(
    val email: String,
    val fcm_token: Any,
    val full_name: String,
    val gender: String,
    val id: Int,
    val is_active: Boolean,
    val refresh_token: String,
    val token: String,
    val verification_code: Any
)