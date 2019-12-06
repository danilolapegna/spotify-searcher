package com.spotifysearch.ui.adapter.listener

import com.spotifysearch.model.SearchItem

interface SearchAdapterListener : BaseRecyclerListener {

    fun onSearchItemClick(element: SearchItem)

}