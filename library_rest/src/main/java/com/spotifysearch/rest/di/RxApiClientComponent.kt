package com.spotifysearch.rest.di

import com.spotifysearch.rest.RxApiClient
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RxApiClientModule::class])
interface RxApiClientComponent {

    fun inject(apiClient: RxApiClient)
}