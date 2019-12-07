package com.spotifysearch.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.spotifysearch.R
import com.spotifysearch.model.SearchItem
import com.spotifysearch.rest.ApplicationRestHeadersProvider
import com.spotifysearch.rest.RxApiClient
import com.spotifysearch.rest.RxApiClientRequestBuilder
import com.spotifysearch.rest.model.ArtistItem
import com.spotifysearch.rest.model.RealmArtist
import com.spotifysearch.rest.model.RealmTrack
import com.spotifysearch.rest.model.SearchResponse
import com.spotifysearch.rest.model.TrackItem
import com.spotifysearch.util.RealmHelper
import com.spotifysearch.util.RealmHelper.persistArtist
import com.spotifysearch.util.RealmHelper.persistTrack
import com.spotifysearch.util.RealmHelper.shouldFetchArtistFromApi
import com.spotifysearch.util.RealmHelper.shouldFetchTrackFromApi
import com.spotifysearch.util.RxRequestHelper
import com.spotifysearch.util.RxRequestHelper.executeRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.RealmResults

/*
 * Load from realm
 */
class ArtistViewModel(val id: String, context: Context) : SpotifyApiViewModel<ArtistItem>(context) {

    val artist: RealmResults<RealmArtist>? by lazy {
        loadArtist()
    }

    private fun loadArtist(): RealmResults<RealmArtist>? {
        val artist = RealmHelper.queryArtist(id)

        /* Populate realm from api if object is incomplete/old. RealmResults will be already subscribed to it */
        if (shouldFetchArtistFromApi(artist?.firstOrNull())) {
            val request = requestBuilder.responseClass(ArtistItem::class.java)
                    .pathParameter(application.getString(R.string.path_param_artist), id)
                    .build()
            executeRequest(request, getArtistResponseListener(), application, queueIfNoNetwork = true)

        }
        return artist
    }

    private fun getArtistResponseListener(): RxRequestHelper.RxRequestListener<ArtistItem> {
        return object : RxRequestHelper.RxRequestListener<ArtistItem>() {

            override fun onRequestSuccess(response: ArtistItem) {
                super.onRequestSuccess(response)
                persistArtist(response)
            }
        }
    }
}

/*
 * Load from realm
 */
class TrackViewModel(val id: String, context: Context) : SpotifyApiViewModel<TrackItem>(context) {

    val track: RealmResults<RealmTrack>? by lazy {
        loadTrack()
    }

    private fun loadTrack(): RealmResults<RealmTrack>? {
        val track = RealmHelper.queryTrack(id)

        /* Populate realm from api if object is incomplete/old. RealmResults will be already subscribed to it */
        if (shouldFetchTrackFromApi(track?.firstOrNull())) {
            val request = requestBuilder.responseClass(TrackItem::class.java)
                    .pathParameter(application.getString(R.string.path_param_track), id)
                    .build()
                    .subscribeOn(Schedulers.io())
            executeRequest(request, getTrackResponseListener(), application, queueIfNoNetwork = true)
        }

        return track
    }

    private fun getTrackResponseListener(): RxRequestHelper.RxRequestListener<TrackItem> {
        return object : RxRequestHelper.RxRequestListener<TrackItem>() {

            override fun onRequestSuccess(response: TrackItem) {
                super.onRequestSuccess(response)
                persistTrack(response)
            }
        }
    }
}

/*
 * Load from api
 */
class SearchViewModel(context: Context) : SpotifyApiViewModel<SearchResponse>(context) {

    private var query: String = ""

    val searchResults: Single<ArrayList<SearchItem>>
        get() = loadSearchResults()

    fun updateQuery(newQuery: String) {
        this.query = newQuery
    }

    private fun loadSearchResults(): Single<ArrayList<SearchItem>> {
        return requestBuilder
                .responseClass(SearchResponse::class.java)
                .additionalUrl(application.getString(R.string.spotify_path_search))
                .queryParameter(application.getString(R.string.spotify_query_param_name), query)
                .queryParameter(application.getString(R.string.spotify_type_param_name), application.getString(R.string.spotify_type_param_value))
                .build()
                .map { value ->
                    val arrayList = ArrayList<SearchItem>()
                    value.artists?.items?.let { artistItems -> arrayList.addAll(artistItems.map { SearchItem(it) }) }
                    value.tracks?.items?.let { trackItems -> arrayList.addAll(trackItems.map { SearchItem(it) }) }
                    arrayList
                }

    }
}

open class SpotifyApiViewModel<T>(context: Context) : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    /* No risk of leak as it's always appContext and not other context */
    protected val application: Context = context.applicationContext

    private val restRxClient = RxApiClient.Builder()
            .baseUrl(context.getString(R.string.spotify_base_url))
            .header(ApplicationRestHeadersProvider(context).getAuthHeader())
            .header(ApplicationRestHeadersProvider(context).getContentTypeHeader())
            .build()

    protected val requestBuilder: RxApiClientRequestBuilder<T>
        get() = RxApiClientRequestBuilder<T>()
                .client(restRxClient)
}