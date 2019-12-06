package com.spotifysearch

import com.spotifysearch.di.DaggerSpotifyAppComponent
import com.spotifysearch.util.ConnectionStateMonitor
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.realm.Realm

class SpotifyApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        ConnectionStateMonitor.enable(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerSpotifyAppComponent
                    .builder()
                    .application(this)
                    .build()
}