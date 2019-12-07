package com.spotifysearch.rest.di

import com.google.gson.Gson
import com.spotifysearch.rest.BaseRestRequestExecutor
import com.spotifysearch.rest.RestHeader
import com.spotifysearch.rest.client.OkHttpRequestExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RxApiClientModule(val baseHeaders: List<RestHeader>?) {

    @Provides
    @Singleton
    open fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    open fun provideExecutor(): BaseRestRequestExecutor = OkHttpRequestExecutor(baseHeaders)
}