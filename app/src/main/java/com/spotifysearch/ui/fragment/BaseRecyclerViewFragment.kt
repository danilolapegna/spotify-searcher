package com.spotifysearch.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spotifysearch.R
import com.spotifysearch.ui.UpdateableListUI
import com.spotifysearch.ui.adapter.RecyclerAdapter
import com.spotifysearch.ui.adapter.RecyclerBaseViewHolder
import com.spotifysearch.ui.adapter.listener.BaseRecyclerListener
import kotlinx.android.synthetic.main.fragment_recycler.emptyView
import kotlinx.android.synthetic.main.fragment_recycler.recycler
import kotlinx.android.synthetic.main.fragment_recycler.swipeRefresh

/*
 * Base, simple, reusable UI component for lists/grids
 */
abstract class BaseRecyclerViewFragment<T, V : RecyclerBaseViewHolder<T>> : Fragment(), UpdateableListUI<T>, BaseRecyclerListener {

    abstract val adapter: RecyclerAdapter<T, V>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getLayoutInflateRes(), container, false)

    open fun getLayoutInflateRes(): Int = R.layout.fragment_recycler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = getLayoutManagerNewInstance(view.context)
        recycler.adapter = adapter
        swipeRefresh.isEnabled = enableSwipeRefresh()
    }

    open fun enableSwipeRefresh(): Boolean = true

    override fun updateElements(elements: List<T>) {
        adapter.updateAdapter(elements)
    }

    /*
     * Can extend for grids
     */
    open fun getLayoutManagerNewInstance(context: Context) = LinearLayoutManager(context)

    /* Default behavior: hide recycler if no items. Can be overriden to change */
    override fun onDataCountChange(size: Int) {
        emptyView.visibility = if (size == 0) View.VISIBLE else View.GONE
        recycler.visibility = if (size == 0) View.GONE else View.VISIBLE
    }

    override fun clearItems() {
        adapter.clearItems()
    }
}