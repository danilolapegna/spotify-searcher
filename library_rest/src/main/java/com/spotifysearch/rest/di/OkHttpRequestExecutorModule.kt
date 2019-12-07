package com.spotifysearch.rest.di

import com.google.gson.Gson
import com.spotifysearch.rest.RestConstants.OKHTTP_CLIENT_TIMEOUT_SECONDS
import com.spotifysearch.rest.RestHeader
import com.spotifysearch.rest.client.OkHttpAddHeaderInterceptor
import com.spotifysearch.rest.client.OkHttpRequestProvider
import com.spotifysearch.rest.enums.BaseOkHttpInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class OkHttpRequestExecutorModule(val baseHeaders: List<RestHeader>? = null) {

    @Provides
    @Singleton
   open fun provideBaseClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .connectTimeout(OKHTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(OKHTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(OKHTTP_CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        BaseOkHttpInterceptor.values().forEach {
            it.getInstance()?.let { interceptor ->
                builder.addInterceptor(interceptor)
            }
        }
        baseHeaders?.let {
            builder.addInterceptor(OkHttpAddHeaderInterceptor(baseHeaders))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    open  fun provideRequestProvider(): OkHttpRequestProvider = OkHttpRequestProvider.instance

    @Provides
    @Singleton
    open  fun provideGson(): Gson = Gson()
}