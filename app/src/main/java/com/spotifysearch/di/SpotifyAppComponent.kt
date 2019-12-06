package com.spotifysearch.di

import com.spotifysearch.SpotifyApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [SpotifyAppModule::class, ActivityModule::class, FragmentModule::class, AndroidSupportInjectionModule::class])

interface SpotifyAppComponent : AndroidInjector<SpotifyApp> {
    @Component.Builder
    interface Builder {
        fun build(): SpotifyAppComponent

        @BindsInstance
        fun application(application: SpotifyApp): Builder
    }
}