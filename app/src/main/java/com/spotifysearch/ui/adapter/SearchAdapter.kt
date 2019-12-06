package com.spotifysearch.ui.adapter

import android.view.View
import com.spotifysearch.R
import com.spotifysearch.model.SearchItem
import com.spotifysearch.rest.model.ArtistItem
import com.spotifysearch.rest.model.TrackItem
import com.spotifysearch.ui.adapter.listener.SearchAdapterListener
import kotlinx.android.synthetic.main.item_search.view.image
import kotlinx.android.synthetic.main.item_search.view.name

class SearchAdapter(private val searchListener: SearchAdapterListener) : RecyclerAdapter<SearchItem, SearchAdapter.SearchViewHolder>(searchListener) {

    override fun getItemLayoutRes(): Int = R.layout.item_search

    override fun buildViewHolderNewInstance(itemView: View): SearchViewHolder = SearchViewHolder(itemView, searchListener)

    class SearchViewHolder(view: View, private val searchListener: SearchAdapterListener) : RecyclerBaseViewHolder<SearchItem>(view) {

        override fun populate(element: SearchItem) {
            itemView.setOnClickListener { searchListener.onSearchItemClick(element) }
            var drawableRes: Int? = null
            when (element.elem) {
                is ArtistItem -> {
                    itemView.name.text = element.elem.name
                    drawableRes = R.drawable.ic_artist
                }
                is TrackItem -> {
                    itemView.name.text = element.elem.name
                    drawableRes = R.drawable.ic_track
                }
                else -> {
                    itemView.name.text = ""
                    itemView.image.setImageDrawable(null)
                }
            }
            drawableRes?.let { resource -> itemView.image.setImageResource(resource) }
        }
    }

}