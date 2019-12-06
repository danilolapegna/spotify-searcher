package com.spotifysearch.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context

class ArtistViewModelFactory(private val id: String, private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = ArtistViewModel(id, applicationContext) as T
}

class TrackViewModelFactory(private val id: String, private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = TrackViewModel(id, applicationContext) as T

}

class SearchViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = SearchViewModel(applicationContext) as T

}