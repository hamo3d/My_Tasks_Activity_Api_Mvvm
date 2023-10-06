package com.example.mytasksactivity.repository

import com.example.mytasksactivity.api.RetrofitInstance

class TasksUserRepository {

    suspend fun getTasks() = RetrofitInstance.api.getTasks()
    suspend fun deleteTasks(id: Int) = RetrofitInstance.api.deleteTasks(id)
    suspend fun addTasks(title: String) = RetrofitInstance.api.addTasks(title)
    suspend fun updateTasks(id: Int, title: String) = RetrofitInstance.api.updateTasks(id, title)
}