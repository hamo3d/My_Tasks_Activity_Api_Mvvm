package com.example.mytasksactivity.prefs

import android.content.Context
import android.content.SharedPreferences
import com.example.mytasksactivity.model.User
import com.example.mytasksactivity.utils.Constants.Companion.EMAIL
import com.example.mytasksactivity.utils.Constants.Companion.FULL_NAME
import com.example.mytasksactivity.utils.Constants.Companion.ID
import com.example.mytasksactivity.utils.Constants.Companion.LOGGED_IN
import com.example.mytasksactivity.utils.Constants.Companion.TOKEN


class AppSharedPreferencesController private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private var instance: AppSharedPreferencesController? = null

        @Synchronized
        fun getInstance(context: Context): AppSharedPreferencesController {
            if (instance == null) {
                instance = AppSharedPreferencesController(context)
            }
            return instance!!
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, "")
    }

    fun save(user: User) {
        editor = sharedPreferences.edit()
        editor?.putBoolean(LOGGED_IN, true)
        editor?.putInt(ID, user.id)
        editor?.putString(FULL_NAME, user.full_name)
        editor?.putString(EMAIL, user.email)
        editor?.putString(TOKEN, "Bearer " + user.token)
        editor?.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(LOGGED_IN, false)
    }

    fun clear() {
        editor = sharedPreferences.edit()
        editor?.clear()
        editor?.apply()
    }
}
