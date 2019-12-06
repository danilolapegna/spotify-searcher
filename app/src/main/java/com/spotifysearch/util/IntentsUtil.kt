package com.spotifysearch.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.spotifysearch.ui.activity.ArtistActivity
import com.spotifysearch.ui.activity.SearchActivity
import com.spotifysearch.ui.activity.TrackActivity

object IntentsUtil {

    fun getViewUrlIntent(context: Context?, url: String?): Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context?.startActivity(intent)
        return startSafely(context, intent)
    }

    fun getViewArtistIntent(context: Context?, id: String?): Intent {
        val intent = Intent(context, ArtistActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_ID, id)
        return startSafely(context, intent)
    }

    fun getViewTrackIntent(context: Context?, id: String?): Intent {
        val intent = Intent(context, TrackActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_ID, id)
        return startSafely(context, intent)
    }

    private fun startSafely(context: Context?, intent: Intent): Intent {
        if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }
}