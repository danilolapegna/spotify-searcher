package com.spotifysearch.rest.di

import com.spotifysearch.rest.client.OkHttpRequestExecutor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [OkHttpRequestExecutorModule::class])
interface OkHttpRequestExecutorComponent {

    fun inject(executor: OkHttpRequestExecutor)
}