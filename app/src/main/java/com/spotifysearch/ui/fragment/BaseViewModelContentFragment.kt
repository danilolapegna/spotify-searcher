package com.spotifysearch.ui.fragment

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.spotifysearch.R
import com.spotifysearch.ui.activity.SearchActivity.Companion.EXTRA_ID
import com.spotifysearch.util.TypefaceManager
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import io.realm.RealmModel
import kotlinx.android.synthetic.main.item_collapsing_toolbar.collapsingToolbar
import kotlinx.android.synthetic.main.item_collapsing_toolbar.toolbar

/*
 * Base, simple, reusable UI component for artist or track
 */
abstract class BaseViewModelContentFragment<T : ViewModel, V : RealmModel> : DaggerFragment() {

    protected lateinit var viewModel: T

    protected lateinit var contentId: String

    protected val actionBarActivity: AppCompatActivity?
        get() = activity as? AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            contentId = it.getString(EXTRA_ID, "")
            if (contentId.isEmpty()) throw IllegalArgumentException("You need to give a valid id")
        }
        viewModel = initViewModel()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                actionBarActivity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarActivity?.setSupportActionBar(toolbar)
        actionBarActivity?.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_left_arrow_white)
        }
        collapsingToolbar.apply {
            setCollapsedTitleTypeface(TypefaceManager.getRegularTypeface(context))
            setExpandedTitleTypeface(TypefaceManager.getBoldTypeface(context))
        }
    }

    protected abstract fun getMainViewModelElem(): V?

    abstract fun initViewModel(): T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(getLayoutInflateRes(), container, false)

    abstract fun getLayoutInflateRes(): Int

    companion object {

        fun <T : DaggerFragment> initWithId(id: String, fragment: T): T {
            return fragment.apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ID, id)
                }
            }
        }
    }
}