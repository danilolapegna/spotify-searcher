package com.spotifysearch.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.spotifysearch.R
import com.spotifysearch.rest.model.RealmArtist
import com.spotifysearch.ui.adapter.listener.BaseRecyclerListener
import com.spotifysearch.ui.runOnUiThread
import com.spotifysearch.util.ImageUtil.loadImageInCollapsingToolbar
import com.spotifysearch.util.IntentsUtil
import com.spotifysearch.util.SpotifyInfoUtil
import com.spotifysearch.viewmodel.ArtistViewModel
import com.spotifysearch.viewmodel.ArtistViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_artist.artistInfoTextView
import kotlinx.android.synthetic.main.fragment_artist.goToArtistInfoTextView
import kotlinx.android.synthetic.main.item_collapsing_toolbar.contentImage
import javax.inject.Inject

class ArtistFragment : BaseViewModelContentFragment<ArtistViewModel, RealmArtist>(), BaseRecyclerListener {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var applicationContext: Context

    override fun initViewModel(): ArtistViewModel = ArtistViewModelFactory(contentId, applicationContext)
            .create(ArtistViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.artist?.addChangeListener { _ -> populateUI() }
    }

    private fun populateUI() {
        runOnUiThread {
            val artist = getMainViewModelElem()
            artist?.let {
                actionBarActivity?.supportActionBar?.title = artist.name
                val infoText = buildInfoText()
                if (infoText.isNullOrEmpty()) {
                    artistInfoTextView.visibility = View.GONE
                } else {
                    artistInfoTextView.visibility = View.VISIBLE
                    artistInfoTextView.text = infoText
                }
                if (artist.externalSpotifyUrl.isNullOrEmpty()) {
                    goToArtistInfoTextView.visibility = View.GONE
                } else {
                    goToArtistInfoTextView.visibility = View.VISIBLE
                    artist.externalSpotifyUrl?.let { url ->
                        goToArtistInfoTextView.setOnClickListener { startActivity(IntentsUtil.getViewUrlIntent(context, url)) }
                    }
                }
                loadImageInCollapsingToolbar(
                        context,
                        picasso,
                        artist.artistImages.firstOrNull(),
                        contentImage)
            }
        }
    }


    private fun buildInfoText(): CharSequence? {
        var infoText: CharSequence? = null
        getMainViewModelElem()?.let { artist ->
            infoText = SpotifyInfoUtil.buildArtistInfoText(context, artist)
        }
        return infoText
    }

    override fun getMainViewModelElem(): RealmArtist? = viewModel.artist?.firstOrNull()

    override fun getLayoutInflateRes(): Int = R.layout.fragment_artist

    companion object {

        fun newInstance(id: String) = initWithId(id, ArtistFragment())
    }
}