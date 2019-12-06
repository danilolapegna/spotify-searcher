package com.spotifysearch.ui.activity

import android.os.Bundle
import com.spotifysearch.R
import com.spotifysearch.ui.fragment.ArtistFragment
import com.spotifysearch.ui.switchFragment
import dagger.android.support.DaggerAppCompatActivity

class ArtistActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_no_toolbar)
        val id = intent.getStringExtra(SearchActivity.EXTRA_ID)
        if (id.isNullOrEmpty()) throw IllegalArgumentException("Id can't be empty")

        if (savedInstanceState == null) switchFragment(ArtistFragment.newInstance(id), R.id.fragmentContainer)
    }
}
