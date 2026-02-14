# ==================================================================
# SENTINELFLOW EXECUTIVE PROTECTION RULES
# ==================================================================

# 1. CORE DATA MODELS (Your Source of Truth)
# Prevents R8 from obfuscating field names used by kotlinx.serialization
-keep class com.sentinel.mobile.models.** { *; }
-keep class com.sentinel.mobile.api.** { *; }
-keepnames class com.sentinel.mobile.api.dto.** { *; }

# 2. RETROFIT & OKHTTP HARDENING
# Essential for your SentinelApiService to map methods to Supabase endpoints
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# 3. KOTLIN SERIALIZATION & REFLECTION
# Prevents "Serializer not found" errors in the release APK
-keepattributes *Annotation*, InnerClasses, EnclosingMethod
-dontnote kotlinx.serialization.AnnotationsKt.**
-keepclassmembers class kotlinx.serialization.** { volatile <fields>; }
-keep,allowobfuscation class kotlinx.serialization.json.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# 4. COROUTINES & LIVE DATA FEED
# Ensures your LiveMonitorViewModel polling doesn't get optimized away
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.android.HandlerContext {
    private volatile y.a._Dispatcher;
}

# 5. BIOMETRIC HARDWARE GATE
# Keeps the native hardware prompt interfaces intact
-keep class androidx.biometric.** { *; }