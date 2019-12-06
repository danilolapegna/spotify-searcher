package com.spotifysearch.di

import com.spotifysearch.ui.fragment.ArtistFragment
import com.spotifysearch.ui.fragment.TrackFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [ArtistFragmentModule::class])
    @FragmentScope
    internal abstract fun artistFragment(): ArtistFragment

    @ContributesAndroidInjector(modules = [TrackFragmentModule::class])
    @FragmentScope
    internal abstract fun trackFragment(): TrackFragment
}

@Module
class ArtistFragmentModule


@Module
class TrackFragmentModule