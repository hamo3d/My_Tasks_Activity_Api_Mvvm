package com.example.mytasksactivity.api

import com.example.mytasksactivity.MyApplication
import com.example.mytasksactivity.prefs.AppSharedPreferencesController
import com.example.mytasksactivity.utils.Constants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val builder: Request.Builder = chain.request().newBuilder()
                    val token = AppSharedPreferencesController.getInstance(MyApplication.getContext()).getToken()
                    if (token!!.isNotEmpty()) {
                        builder.addHeader("Authorization", token)
                    }
                    builder.addHeader("Accept", "application/json")
                    builder.addHeader("Content-Type", "application/json")
                    chain.proceed(builder.build())
                }
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}
