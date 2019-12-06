package com.spotifysearch.di

import com.spotifysearch.ui.activity.ArtistActivity
import com.spotifysearch.ui.activity.LoginActivity
import com.spotifysearch.ui.activity.SearchActivity
import com.spotifysearch.ui.activity.TrackActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    @ActivityScope
    internal abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [SearchActivityModule::class])
    @ActivityScope
    internal abstract fun searchActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [ArtistActivityModule::class])
    @ActivityScope
    internal abstract fun artistActivity(): ArtistActivity

    @ContributesAndroidInjector(modules = [TrackActivityModule::class])
    @ActivityScope
    internal abstract fun trackActivity(): TrackActivity
}

@Module
class LoginActivityModule


@Module
class SearchActivityModule


@Module
class ArtistActivityModule


@Module
class TrackActivityModule