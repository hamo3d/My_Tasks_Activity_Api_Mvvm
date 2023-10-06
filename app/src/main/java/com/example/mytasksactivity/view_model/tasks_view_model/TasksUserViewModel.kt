package com.example.mytasksactivity.view_model.tasks_view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytasksactivity.MyApplication
import com.example.mytasksactivity.model.BaseResponse
import com.example.mytasksactivity.model.TasksUser
import com.example.mytasksactivity.repository.TasksUserRepository
import com.example.mytasksactivity.utils.Constants.Companion.CONSTANTS_ERROR
import com.example.mytasksactivity.utils.Constants.Companion.MESSAGE
import com.example.mytasksactivity.utils.Constants.Companion.NETWORK_FAILURE
import com.example.mytasksactivity.utils.Constants.Companion.NO_INTERNET
import com.example.mytasksactivity.utils.Resources
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.nio.charset.StandardCharsets

class TasksUserViewModel(
    application: Application, private val tasksUserRepository: TasksUserRepository
) : AndroidViewModel(application) {

    val tasks: MutableLiveData<Resources<BaseResponse<MutableList<TasksUser>>>> = MutableLiveData()
    val delete: MutableLiveData<Resources<BaseResponse<TasksUser>>> = MutableLiveData()
    val addTasks: MutableLiveData<Resources<BaseResponse<TasksUser>>> = MutableLiveData()

    /**
     * Get Data
     */

    fun getTasksUser() {
        viewModelScope.launch {
            safeGetTasksUsersNewsCall()
        }
    }

    private suspend fun safeGetTasksUsersNewsCall() {
        tasks.postValue(Resources.loading())
        try {
            if (hasInternetConnection()) {
                val response = tasksUserRepository.getTasks()
                tasks.postValue(handleGetTasksUserResponse(response))
            } else {
                tasks.postValue(Resources.Error(NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> tasks.postValue(Resources.Error(NETWORK_FAILURE))
                else -> tasks.postValue(Resources.Error(CONSTANTS_ERROR))
            }
        }
    }

    private fun handleGetTasksUserResponse(response: Response<BaseResponse<MutableList<TasksUser>>>): Resources<BaseResponse<MutableList<TasksUser>>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString(MESSAGE))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.message())
    }

    /**
     * Delete Data
     */

    fun deleteTasksUser(id: Int) = viewModelScope.launch {
        safeDeleteTasksUsersNewsCall(id)
    }

    private suspend fun safeDeleteTasksUsersNewsCall(id: Int) {
        try {
            if (hasInternetConnection()) {
                val response = tasksUserRepository.deleteTasks(id)
                delete.postValue(handleDeleteTasksUserResponse(response))
            } else {
                delete.postValue(Resources.Error(NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> delete.postValue(Resources.Error(NETWORK_FAILURE))
                else -> delete.postValue(Resources.Error(CONSTANTS_ERROR))
            }
        }
    }

    private fun handleDeleteTasksUserResponse(response: Response<BaseResponse<TasksUser>>): Resources<BaseResponse<TasksUser>> {

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString(MESSAGE))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.message())
    }

    /**
     * Add Tasks
     */

    fun addTasksUser(title: String) = viewModelScope.launch {
        safeAddTasksUserResponse(title)
    }

    private suspend fun safeAddTasksUserResponse(title: String) {
        addTasks.postValue(Resources.loading())
        try {
            if (hasInternetConnection()) {
                val response = tasksUserRepository.addTasks(title)
                addTasks.postValue(handleAddTasksUserResponse(response))
            } else {
                addTasks.postValue(Resources.Error(NO_INTERNET))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> addTasks.postValue(Resources.Error(NETWORK_FAILURE))
                else -> addTasks.postValue(Resources.Error(CONSTANTS_ERROR))
            }
        }
    }

    private fun handleAddTasksUserResponse(response: Response<BaseResponse<TasksUser>>): Resources<BaseResponse<TasksUser>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString(MESSAGE))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.message())
    }

    /**
     * update tasks
     */
    fun updateTasks(id: Int, title: String) = viewModelScope.launch {
        safeUpdateTasksUserResponse(id, title)
    }


    private suspend fun safeUpdateTasksUserResponse(id: Int, title: String) {
        try {
            if (hasInternetConnection()) {
                val response = tasksUserRepository.updateTasks(id, title)
                addTasks.postValue(handleUpdateTasksUserResponse(response))
            } else {
                addTasks.postValue(Resources.Error(NO_INTERNET))

            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> addTasks.postValue(Resources.Error(NETWORK_FAILURE))
                else -> addTasks.postValue(Resources.Error(CONSTANTS_ERROR))
            }
        }
    }


    private fun handleUpdateTasksUserResponse(response: Response<BaseResponse<TasksUser>>): Resources<BaseResponse<TasksUser>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString(MESSAGE))

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.message())
    }


    private fun hasInternetConnection(): Boolean {
        val myApplication = MyApplication.getContext()
        val connectivityManager =
            myApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
}
