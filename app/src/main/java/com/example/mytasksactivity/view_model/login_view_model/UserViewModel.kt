package com.example.mytasksactivity.view_model.login_view_model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytasksactivity.MyApplication
import com.example.mytasksactivity.model.BaseResponse
import com.example.mytasksactivity.model.User
import com.example.mytasksactivity.prefs.AppSharedPreferencesController
import com.example.mytasksactivity.repository.UserRepository
import com.example.mytasksactivity.utils.Resources
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.nio.charset.StandardCharsets

class UserViewModel(application: Application, private val userRepository: UserRepository) :
    AndroidViewModel(application) {
    private val appSharedPreferencesController: AppSharedPreferencesController =
        AppSharedPreferencesController.getInstance(getApplication<MyApplication>())

    val users: MutableLiveData<Resources<BaseResponse<User>>> = MutableLiveData()


    /**
     * Login User
     */
    fun login(email: String, password: String) = viewModelScope.launch {
        safeLoginUsersNewsCall(email, password)
    }

    private suspend fun safeLoginUsersNewsCall(email: String, password: String) {
        users.postValue(Resources.loading())
        try {
            if (hasInternetConnection()) {
                val response = userRepository.login(email, password)
                users.postValue(handleLoginTasksResponse(response))
            } else {
                users.postValue(Resources.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> users.postValue(Resources.Error("Network Failure"))
                else -> users.postValue(Resources.Error("Conversion Error"))
            }

        }

    }

    private fun handleLoginTasksResponse(response: Response<BaseResponse<User>>): Resources<BaseResponse<User>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                saveUserInSharedPreferences(resultResponse.`object`)
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString("message"))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.message())

    }

    private fun saveUserInSharedPreferences(user: User) {
        appSharedPreferencesController.save(user)
    }

    /**
     * register User
     */

    fun register(fullName: String, email: String, password: String, gender: String) =
        viewModelScope.launch {
            safeRegisterUsersNewsCall(fullName, email, password, gender)
        }

    private suspend fun safeRegisterUsersNewsCall(
        fullName: String,
        email: String,
        password: String,
        gender: String
    ) {
        users.postValue(Resources.loading())
        try {
            if (hasInternetConnection()) {
                val response = userRepository.register(fullName, email, password, gender)
                users.postValue(handleRegisterTasksResponse(response))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> users.postValue(Resources.Error("Network Failure"))
                else -> users.postValue(Resources.Error("Conversion Error"))
            }
        }
    }

    private fun handleRegisterTasksResponse(response: Response<BaseResponse<User>>): Resources<BaseResponse<User>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        } else {
            try {
                val error = String(response.errorBody()!!.bytes(), StandardCharsets.UTF_8)
                val jsonObject = JSONObject(error)
                return Resources.Error(jsonObject.getString("message"))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return Resources.Error(response.body().toString())
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