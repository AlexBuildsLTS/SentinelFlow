package com.sentinel.mobile.api

// ✅ CRITICAL FIX: This import will now work correctly because you fixed the namespace in build.gradle.kts.
import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sentinel.mobile.auth.SessionManager
import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.annotations.SupabaseInternal
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


@OptIn(SupabaseInternal::class)
val BuildConfig.BUG: Boolean
    get() = true


object ApiClient {
    private lateinit var apiService: SentinelApiService

    @OptIn(SupabaseInternal::class)
    fun init(context: Context) {
        if (::apiService.isInitialized) return

        val sessionManager = SessionManager(context.applicationContext)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.BUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val jsonConfig = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SupabaseConfig.URL) // Ensure SupabaseConfig.URL is correct
            .client(okHttpClient)
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()

        apiService = retrofit.create(SentinelApiService::class.java)
    }

    val service: SentinelApiService
        get() {
            if (!::apiService.isInitialized) {
                throw IllegalStateException("ApiClient must be initialized in Application#onCreate()")
            }
            return apiService
        }
}
