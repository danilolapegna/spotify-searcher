package com.spotifysearch.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import com.spotifysearch.R
import com.spotifysearch.rest.model.RealmTrack
import com.spotifysearch.ui.runOnUiThread
import com.spotifysearch.util.ImageUtil.loadImageInCollapsingToolbar
import com.spotifysearch.util.IntentsUtil.getViewUrlIntent
import com.spotifysearch.util.SpannableUtil.makeClickable
import com.spotifysearch.util.SpotifyInfoUtil.buildTrackInfoText
import com.spotifysearch.util.StringUtil.toReadableTime
import com.spotifysearch.viewmodel.TrackViewModel
import com.spotifysearch.viewmodel.TrackViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_track.trackDurationTextView
import kotlinx.android.synthetic.main.fragment_track.trackInfoTextView
import kotlinx.android.synthetic.main.fragment_track.trackPlayButton
import kotlinx.android.synthetic.main.item_collapsing_toolbar.contentImage
import javax.inject.Inject

class TrackFragment : BaseViewModelContentFragment<TrackViewModel, RealmTrack>() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var applicationContext: Context

    override fun initViewModel(): TrackViewModel = TrackViewModelFactory(contentId, applicationContext)
            .create(TrackViewModel::class.java)

    override fun getLayoutInflateRes(): Int = R.layout.fragment_track

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.track?.addChangeListener { _ -> populateUI() }
    }

    private fun populateUI() {
        runOnUiThread {
            val track = getMainViewModelElem()
            track?.let {
                actionBarActivity?.supportActionBar?.title = track.name
                val timeText = toReadableTime(context, track.trackDurationMs)

                val infoText = buildInfoText()
                if (infoText.isNullOrEmpty()) {
                    trackInfoTextView.visibility = View.GONE
                } else {
                    trackInfoTextView.movementMethod = LinkMovementMethod.getInstance()
                    trackInfoTextView.visibility = View.VISIBLE
                    trackInfoTextView.text = infoText
                }
                if (track.externalSpotifyUrl.isNullOrEmpty()) {
                    trackPlayButton.visibility = View.GONE
                    trackDurationTextView.text = timeText
                } else {
                    trackPlayButton.visibility = View.VISIBLE
                    track.externalSpotifyUrl?.let { url ->
                        trackDurationTextView.movementMethod = LinkMovementMethod.getInstance()
                        val afterDurationClickable = makeClickable(getString(R.string.play_spotify_page)) { startActivity(getViewUrlIntent(context, url)) }
                        trackDurationTextView.text = TextUtils.concat(timeText, "\n", afterDurationClickable)
                        trackPlayButton.setOnClickListener { startActivity(getViewUrlIntent(context, url)) }
                    }
                }
                loadImageInCollapsingToolbar(context,
                        picasso,
                        getMainViewModelElem()?.albumImages?.firstOrNull(),
                        contentImage)
            }
        }
    }

    private fun buildInfoText(): CharSequence? {
        var infoText: CharSequence? = null
        getMainViewModelElem()?.let { track ->
            infoText = buildTrackInfoText(context, track)
        }
        return infoText
    }

    override fun getMainViewModelElem(): RealmTrack? = viewModel.track?.firstOrNull()


    companion object {

        fun newInstance(id: String) = initWithId(id, TrackFragment())
    }
}