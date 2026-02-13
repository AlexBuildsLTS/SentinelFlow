package com.sentinel.mobile.api

import android.content.Context
import com.sentinel.mobile.auth.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // ✅ This requires the Gradle fix


object ApiClient {
    private const val BASE_URL = "https://puqqnwwkouiulhibvdkn.supabase.co"

    private lateinit var retrofit: Retrofit
    lateinit var service: SentinelApiService

    fun init(context: Context) {
        val sessionManager = SessionManager(context)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(SentinelApiService::class.java)

    }
}