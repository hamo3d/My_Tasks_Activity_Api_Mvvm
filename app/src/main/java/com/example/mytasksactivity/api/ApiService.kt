package com.example.mytasksactivity.api

import com.example.mytasksactivity.model.BaseResponse
import com.example.mytasksactivity.model.TasksUser
import com.example.mytasksactivity.model.User
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("students/auth/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse<User>>


    @POST("students/auth/register")
    @FormUrlEncoded()
    suspend fun register(
        @Field("full_name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String
    ): Response<BaseResponse<User>>


    /**
     * Tasks
     */

    @GET("tasks")
    suspend fun getTasks(): Response<BaseResponse<MutableList<TasksUser>>>

    @DELETE("tasks/{id}")
    suspend fun deleteTasks(@Path("id") id: Int): Response<BaseResponse<TasksUser>>


    @POST("tasks")
    @FormUrlEncoded
    suspend fun addTasks(@Field("title") title: String): Response<BaseResponse<TasksUser>>

    @PUT("tasks/{id}")
    @FormUrlEncoded
    suspend fun updateTasks(
        @Path("id") id: Int,
        @Field("title") title: String
    ): Response<BaseResponse<TasksUser>>
}