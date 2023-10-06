package com.example.mytasksactivity.model

import com.google.gson.annotations.SerializedName

data class AddBaseResponse(
    @SerializedName("message")
    val message: String,
    val status: Boolean,
    val `data`: TasksUser,

)