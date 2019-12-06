package com.spotifysearch.util

import android.content.Context
import android.text.SpannableStringBuilder
import com.spotifysearch.R
import com.spotifysearch.rest.model.RealmArtist
import com.spotifysearch.rest.model.RealmTrack
import com.spotifysearch.util.SpannableUtil.makeClickable
import com.spotifysearch.util.SpannableUtil.setBold
import com.spotifysearch.util.SpannableUtil.setNormal

object SpotifyInfoUtil {

    /*
     * Convenience to get readable info out of a track item
     */
    fun buildTrackInfoText(context: Context?, track: RealmTrack): CharSequence {
        val builder = SpannableStringBuilder()
        if (context != null) {
            builder.append(setBold(context.getString(R.string.full_name_format), context)); builder.append(setBold(track.name, context))
            builder.append("\n"); builder.append("\n")
            track.albumName?.let { name ->
                builder.append(setBold(context.getString(R.string.album_format), context)); builder.append(setNormal(name, context))
                builder.append("\n");
            }
            track.albumTrackNumber?.let { number ->
                builder.append(setBold(context.getString(R.string.track_number_format), context)); builder.append(setNormal(number.toString(), context))
                builder.append("\n"); builder.append("\n")
            }
            if (track.artists.isNotEmpty()) {
                builder.append(setBold(context.getString(R.string.by_artist_format), context))
                track.artists.forEach {
                    val artistNameSpannable = makeClickable(setBold(it.name, context)) {
                        context.startActivity(IntentsUtil.getViewArtistIntent(context, it.id))
                    }
                    builder.append(artistNameSpannable)
                    if (it != track.artists.last()) builder.append(context.getString(R.string.list_comma_format))
                }
            }
        }
        return builder
    }

    /*
     * Convenience to get readable info out of an artist item
     */
    fun buildArtistInfoText(context: Context?, artist: RealmArtist): CharSequence {
        val builder = SpannableStringBuilder()
        if (context != null) {
            builder.append(setBold(context.getString(R.string.genre_format), context))
            if (artist.genres.isNotEmpty()) {
                artist.genres.forEach {
                    builder.append(setNormal(it, context))
                    if (it != artist.genres.last()) builder.append(context.getString(R.string.list_comma_format))
                }
            } else {
                builder.append(setNormal(context.getString(R.string.genres_unknown), context))
            }
        }
        return builder
    }
}