package com.spotifysearch.rest.di

import com.google.gson.Gson
import com.spotifysearch.rest.BaseRestRequestExecutor
import com.spotifysearch.rest.RestHeader
import com.spotifysearch.rest.client.OkHttpRequestExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RxApiClientModule(val baseHeaders: List<RestHeader>?) {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideExecutor(): BaseRestRequestExecutor = OkHttpRequestExecutor(baseHeaders)
}