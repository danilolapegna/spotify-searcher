package com.spotifysearch.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spotifysearch.ui.adapter.listener.BaseRecyclerListener

/*
 * Generic base adapter. Can be reused and extended to build different kind of list/grid adapters
 */
abstract class RecyclerAdapter<T, V : RecyclerBaseViewHolder<T>>(var listener: BaseRecyclerListener, var items: List<T> = listOf(), registerObserver: Boolean = true) : RecyclerView.Adapter<V>() {

    init {
        if (registerObserver) registerAdapterDataObserver(getDefaultAdapterDataObserver())
    }

    abstract fun getItemLayoutRes(): Int

    abstract fun buildViewHolderNewInstance(itemView: View): V

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): V {
        val itemView = LayoutInflater.from(p0.context).inflate(getItemLayoutRes(), p0, false)
        return buildViewHolderNewInstance(itemView)
    }

    override fun onBindViewHolder(p0: V, p1: Int) {
        items.getOrNull(p1)?.let { element -> p0.populate(element) }
    }

    override fun getItemCount(): Int = items.size

    fun updateAdapter(newItems: List<T>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    /*
     * Allow registering callbacks for data change
     */
    private fun getDefaultAdapterDataObserver(): RecyclerView.AdapterDataObserver {
        return object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.GENERIC_CHANGE)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.RANGE_REMOVED,
                        positionStart = positionStart,
                        itemCount = itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.RANGE_MOVED,
                        positionStart = fromPosition,
                        positionEnd = toPosition,
                        itemCount = itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.RANGE_INSERTED,
                        positionStart = positionStart,
                        itemCount = itemCount
                )
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.RANGE_CHANGED,
                        positionStart = positionStart,
                        itemCount = itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                this@RecyclerAdapter.onDataChanged(dataChangedOperation = DataChangedOperation.RANGE_CHANGED,
                        positionStart = positionStart,
                        itemCount = itemCount,
                        payLoad = payload)
            }
        }
    }

    open fun onDataChanged(dataChangedOperation: DataChangedOperation = DataChangedOperation.GENERIC_CHANGE,
                           positionStart: Int? = null,
                           itemCount: Int? = null,
                           positionEnd: Int? = null,
                           payLoad: Any? = null) {

        onDataCountChange()
        /* Override to define granular behaviour */
    }

    open fun onDataCountChange() {

        /* Override to define specific behaviour */
        listener.onDataCountChange(items.size)
    }

    enum class DataChangedOperation {
        GENERIC_CHANGE,
        RANGE_REMOVED,
        RANGE_MOVED,
        RANGE_INSERTED,
        RANGE_CHANGED
    }

    /* Called in constructor. Prevent override and avoid breaking inheritance contract */
    final override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(observer)
    }

    fun clearItems() {
        if (items is MutableList) {
            (items as? MutableList)?.clear()
        } else {
            items = listOf()
        }
        notifyDataSetChanged()
    }
}

abstract class RecyclerBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun populate(element: T)

}