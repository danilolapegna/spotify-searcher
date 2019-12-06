package com.spotifysearch.rest.enums

import com.spotifysearch.rest.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

enum class BaseOkHttpInterceptor {
    LOGGING {
        override fun getInstance(): Interceptor? {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            val logs = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            httpLoggingInterceptor.level = logs
            return httpLoggingInterceptor
        }
    };

    abstract fun getInstance(): Interceptor?

}