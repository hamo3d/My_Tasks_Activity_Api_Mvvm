package com.example.mytasksactivity.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("message")
    val message: String,
    val `object`: User,
    val status: Boolean,
    val data: T,
)