package com.sentinel.mobile.api

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseConfig {
    // ⚡ REAL CREDENTIALS (from your uploaded project files)
    const val URL = "https://puqqnwwkouiulhibvdkn.supabase.co"
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB1cXFud3drb3VpdWxoaWJ2ZGtuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzA4OTg5OTIsImV4cCI6MjA4NjQ3NDk5Mn0.EXotu_PE7DYkYlBqBBhVYWXwGot_wOXDK1HihOvOxc0"

    // Storage Buckets
    const val BUCKET_AVATARS = "avatars"
    const val BUCKET_ATTACHMENTS = "attachments"

    // The Client Instance
    val client = createSupabaseClient(
        supabaseUrl = URL,
        supabaseKey = API_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)

        // JSON Configuration to handle new DB fields gracefully
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        })
    }
}