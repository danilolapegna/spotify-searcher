package com.spotifysearch.ui.activity

import android.os.Bundle
import com.spotifysearch.R
import com.spotifysearch.ui.activity.SearchActivity.Companion.EXTRA_ID
import com.spotifysearch.ui.fragment.TrackFragment
import com.spotifysearch.ui.switchFragment
import dagger.android.support.DaggerAppCompatActivity

class TrackActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_no_toolbar)
        val id = intent.getStringExtra(EXTRA_ID)
        if (id.isNullOrEmpty()) throw IllegalArgumentException("Id can't be empty")

        if (savedInstanceState == null) switchFragment(TrackFragment.newInstance(id), R.id.fragmentContainer)
    }
}
