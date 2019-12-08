package com.spotifysearch.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.mancj.materialsearchbar.MaterialSearchBar
import com.spotifysearch.R
import com.spotifysearch.SharedPreferences
import com.spotifysearch.model.SearchItem
import com.spotifysearch.rest.exceptions.HttpException
import com.spotifysearch.ui.UpdateableListUI
import com.spotifysearch.ui.displayToast
import com.spotifysearch.ui.fragment.SearchRecyclerFragment
import com.spotifysearch.ui.getFragmentInContainer
import com.spotifysearch.ui.hideInputMethod
import com.spotifysearch.ui.switchFragment
import com.spotifysearch.util.NetworkConnectionUtils
import com.spotifysearch.util.RxRequestHelper
import com.spotifysearch.util.RxRequestHelper.executeRequest
import com.spotifysearch.util.TypefaceManager
import com.spotifysearch.viewmodel.SearchViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.searchBar
import kotlinx.android.synthetic.main.activity_search.toolbar
import java.net.HttpURLConnection
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity(), SearchRecyclerFragment.Listener, MaterialSearchBar.OnSearchActionListener {

    override var lastQuery: String = ""

    @Inject
    lateinit var searchPublishSubject: PublishSubject<String>

    private lateinit var searchPublishSubjectSubscription: Disposable

    @Inject
    lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) switchFragment(SearchRecyclerFragment.newInstance(), R.id.fragmentContainer)
        initToolbar()
        initSearchPublishSubject()
    }

    private fun initToolbar() {
        searchBar?.addTextChangeListener(getSearchTextWatcher())
        searchBar?.setOnSearchActionListener(this)
        searchBar?.searchEditText?.typeface = TypefaceManager.getRegularTypeface(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                logout(R.string.successful_logout)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!searchPublishSubjectSubscription.isDisposed) searchPublishSubjectSubscription.dispose()
    }

    private fun initSearchPublishSubject() {

        /* Create an observable debouncing the subject items, in order to avoid doing useless api calls while typing */
        val searchObservable = searchPublishSubject
                .debounce(SEARCH_DEBOUNCE_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
        searchPublishSubjectSubscription = searchObservable.subscribe { query -> doSearch(query) }
    }

    private fun getSearchTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchPublishSubject.onNext(((s ?: "").trim()).toString())
            }
        }
    }

    private fun doSearch(query: String) {
        lastQuery = query
        if (query.isNotEmpty()) {
            searchViewModel.updateQuery(query)
            val request = searchViewModel.searchResults
            executeRequest(request, getRequestListener(query), this, true)
        } else {
            getCurrentSearchFragment()?.clearItems()
        }
    }

    private fun getRequestListener(requestQuery: String): RxRequestHelper.RxRequestListener<ArrayList<SearchItem>> {
        return object : RxRequestHelper.RxRequestListener<ArrayList<SearchItem>>() {

            override fun onRequestStart(d: Disposable) {
                runOnUiThread {
                    getCurrentSearchFragment()?.onRequestStart()
                }
            }

            override fun onRequestSuccess(response: ArrayList<SearchItem>) {
                super.onRequestSuccess(response)
                runOnUiThread {
                    getCurrentSearchFragment()?.onRequestStop()
                    if (requestQuery == lastQuery) {
                        /* Update only if this is the response associated to last query */
                        getCurrentSearchFragment()?.updateElements(response)
                    }
                }
            }

            override fun onRequestError(throwable: Throwable) {
                runOnUiThread {
                    getCurrentSearchFragment()?.onRequestStop()
                    when {
                        throwable is HttpException && throwable.code == HttpURLConnection.HTTP_UNAUTHORIZED -> {

                            /* Need to regenerate token. Logout */
                            logout(R.string.auth_error)
                        }
                        !NetworkConnectionUtils.isNetworkConnected(this@SearchActivity) -> {
                            displayToast(R.string.unavailable_network_error)
                        }
                        else -> {
                            displayToast(R.string.search_error)
                        }
                    }
                }
            }
        }
    }

    override fun onButtonClicked(buttonCode: Int) {}

    override fun onSearchStateChanged(enabled: Boolean) {}

    override fun onSearchConfirmed(text: CharSequence?) {
        hideInputMethod()
    }

    @SuppressWarnings("Unchecked")
    private fun getCurrentSearchFragment() = getFragmentInContainer() as? UpdateableListUI<SearchItem>

    private fun logout(logoutMessageRes: Int) {
        displayToast(logoutMessageRes)
        sharedPreferences.removeAuthToken()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {

        const val EXTRA_ID = "extra_id"

        const val SEARCH_DEBOUNCE_MS = 500L
    }
}
