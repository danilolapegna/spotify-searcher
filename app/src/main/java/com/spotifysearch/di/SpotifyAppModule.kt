package com.spotifysearch.di

import android.content.Context
import com.spotifysearch.SharedPreferences
import com.spotifysearch.SpotifyApp
import com.spotifysearch.viewmodel.SearchViewModel
import com.spotifysearch.viewmodel.SearchViewModelFactory
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class SpotifyAppModule {

    @Provides
    fun provideApplicationContext(application: SpotifyApp): Context = application.applicationContext

    @Provides
    fun provideSharedPreferences(context: Context) = SharedPreferences(context)

    @Provides
    fun provideRxSearchObservable(): PublishSubject<String> = PublishSubject.create<String>()

    @Provides
    fun provideSearchViewModel(application: SpotifyApp): SearchViewModel = SearchViewModelFactory(application).create(SearchViewModel::class.java)

    @Provides
    fun providePicasso(context: Context): Picasso = Picasso.with(context)

}