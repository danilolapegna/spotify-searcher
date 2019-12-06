package com.spotifysearch.util

import android.util.Log
import com.spotifysearch.rest.model.ArtistItem
import com.spotifysearch.rest.model.Image
import com.spotifysearch.rest.model.RealmArtist
import com.spotifysearch.rest.model.RealmImage
import com.spotifysearch.rest.model.RealmTrack
import com.spotifysearch.rest.model.TrackItem
import com.spotifysearch.util.TimeUtils.ONE_DAY_MS
import com.spotifysearch.util.TimeUtils.getCurrentDate
import com.spotifysearch.util.TimeUtils.timeIsOlderThan
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults

object RealmHelper {

    private const val ID_FIELD = "id"

    private val TAG = RealmHelper::class.simpleName

    fun persistArtist(
            artist: ArtistItem?
    ) {
        if (artist != null) {
            val realmObject = generateRealmArtist(artist)
            if (realmObject.id.isNotEmpty()) {
                getRealm().executeTransaction {
                    it.insertOrUpdate(realmObject)
                }
            } else {
                Log.d(TAG, "Can't persist item with empty id")
            }
        }
    }

    fun persistTrack(
            track: TrackItem?
    ) {
        if (track != null) {
            val realmObject = generateRealmTrack(track)
            if (realmObject.id.isNotEmpty()) {
                getRealm().executeTransaction {
                    it.insertOrUpdate(realmObject)
                }
            } else {
                Log.d(TAG, "Can't persist item with empty id")
            }
        }
    }

    fun shouldFetchArtistFromApi(artist: RealmArtist?): Boolean = artist == null || artist.artistImages.isEmpty() || timeIsOlderThan(artist.storedAt.time, ONE_DAY_MS)

    fun shouldFetchTrackFromApi(track: RealmTrack?): Boolean = track == null || timeIsOlderThan(track.storedAt.time, ONE_DAY_MS)

    private fun generateRealmTrack(track: TrackItem): RealmTrack {
        return RealmTrack().apply {
            id = track.id ?: ""
            name = track.name
            albumTrackNumber = track.track_number
            albumName = track.album?.name
            externalSpotifyUrl = track.external_urls?.spotify
            track.artists?.let { trackArtists -> artists.addAll(trackArtists.map { artist -> generateRealmArtist(artist) }) }
            track.album?.images?.let { trackAlbumImages -> albumImages.addAll(trackAlbumImages.map { image -> generateRealmImage(image) }) }
            trackDurationMs = track.duration_ms?.toLong()
            storedAt = getCurrentDate()
        }
    }

    private fun generateRealmArtist(artist: ArtistItem): RealmArtist {
        return RealmArtist().apply {
            id = artist.id ?: ""
            name = artist.name
            externalSpotifyUrl = artist.external_urls?.spotify
            artist.images?.let { apiArtistImages -> artistImages.addAll(apiArtistImages.map { image -> generateRealmImage(image) }) }
            artist.genres?.let { artistGenres -> genres.addAll(artistGenres) }
            storedAt = getCurrentDate()
        }
    }

    private fun generateRealmImage(image: Image): RealmImage {
        return RealmImage().apply {
            url = image.url
            height = image.height
            width = image.width
            id = "${url ?: ""}$height$width"
            storedAt = getCurrentDate()
        }
    }

    fun queryArtist(id: String) = queryById(id, RealmArtist::class.java)

    fun queryTrack(id: String) = queryById(id, RealmTrack::class.java)

    private fun <T : RealmModel> queryById(id: String, schemaClass: Class<T>): RealmResults<T>? =
            getRealm().where(schemaClass)
                    .equalTo(ID_FIELD, id)
                    .findAllAsync()

    private fun getRealm() = Realm.getInstance(RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build())

}