package com.spotifysearch.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.spotifysearch.R
import com.spotifysearch.model.SearchItem
import com.spotifysearch.rest.model.ArtistItem
import com.spotifysearch.rest.model.TrackItem
import com.spotifysearch.ui.SearchFragmentUI
import com.spotifysearch.ui.adapter.RecyclerAdapter
import com.spotifysearch.ui.adapter.SearchAdapter
import com.spotifysearch.ui.adapter.listener.SearchAdapterListener
import com.spotifysearch.util.IntentsUtil.getViewArtistIntent
import com.spotifysearch.util.IntentsUtil.getViewTrackIntent
import com.spotifysearch.util.RealmHelper.persistArtist
import com.spotifysearch.util.RealmHelper.persistTrack
import com.spotifysearch.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_recycler.emptyView
import kotlinx.android.synthetic.main.fragment_recycler.progress
import kotlinx.android.synthetic.main.fragment_recycler.swipeRefresh

class SearchRecyclerFragment : BaseRecyclerViewFragment<SearchItem, SearchAdapter.SearchViewHolder>(),
        SearchAdapterListener,
        SearchFragmentUI {

    override val adapter: RecyclerAdapter<SearchItem, SearchAdapter.SearchViewHolder> = SearchAdapter(this)

    private var searchResponse: SearchViewModel.ViewModelSearchResponse? = null

    private var listener: Listener? = null

    interface Listener {

        var lastQuery: String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as? Listener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyView?.setText(R.string.no_results_no_query)
        emptyView?.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0)
        emptyView?.compoundDrawablePadding = resources.getDimension(R.dimen.default_margin).toInt()
    }

    override fun onSearchItemClick(element: SearchItem) {
        val intent: Intent
        when (element.elem) {
            is ArtistItem -> {
                persistArtist(element.elem)
                intent = getViewArtistIntent(context, element.elem.id)
            }
            is TrackItem -> {
                persistTrack(element.elem)
                intent = getViewTrackIntent(context, element.elem.id)
            }
            else -> throw IllegalArgumentException("This element isn't an artist or track")
        }
        startActivity(intent)
    }

    override fun onDataCountChange(size: Int) {
        super<BaseRecyclerViewFragment>.onDataCountChange(size)
        if (size == 0) {
            emptyView.setText(if (listener?.lastQuery.isNullOrEmpty()) R.string.no_results_no_query else R.string.no_results)
        } else {
            progress.visibility = View.GONE
        }
    }

    override fun onRequestStart() {
        if (adapter.itemCount == 0) {
            progress.visibility = View.VISIBLE
        } else {
            swipeRefresh.isRefreshing = true
        }
    }

    override fun hasResponseForQuery(query: String): Boolean = searchResponse?.query == query && searchResponse?.results?.isNotEmpty() == true

    override fun onRequestStop() {
        swipeRefresh.isRefreshing = false
        progress.visibility = View.GONE
    }

    override fun setSearchResponse(searchResponse: SearchViewModel.ViewModelSearchResponse) {
        this.searchResponse = searchResponse
        adapter.updateAdapter(searchResponse.results)
    }

    companion object {

        fun newInstance() = SearchRecyclerFragment()

    }
}