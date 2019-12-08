package com.spotifysearch.ui

import com.spotifysearch.model.SearchItem
import com.spotifysearch.viewmodel.SearchViewModel

interface SearchFragmentUI : UpdateableListUI<SearchItem> {

    fun setSearchResponse(searchResponse: SearchViewModel.ViewModelSearchResponse)

    fun hasResponseForQuery(query: String): Boolean
}