package com.spotifysearch.rest.model

import java.io.Serializable

class SearchResponse(
        val artists: Artists? = null,
        val tracks: Tracks? = null
) : Serializable

class Tracks(
        val href: String,
        val items: ArrayList<TrackItem> = ArrayList(),
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int
) : Serializable

class TrackItem(
        val album: Album? = null,
        val artists: ArrayList<ArtistItem>? = ArrayList(),
        val disc_number: Int? = null,
        val duration_ms: Int? = null,
        val explicit: Boolean? = null,
        val external_ids: ExternalIds? = null,
        val external_urls: ExternalUrls? = null,
        val href: String? = null,
        val id: String? = null,
        val is_local: Boolean? = null,
        val is_playable: Boolean? = null,
        val name: String? = null,
        val popularity: Int? = null,
        val preview_url: String? = null,
        val track_number: Int? = null,
        val type: String? = null,
        val uri: String? = null
) : Serializable

class Album(
        val album_type: String,
        val artists: ArrayList<ArtistItem> = ArrayList(),
        val genres: ArrayList<String> = ArrayList(),
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val images: ArrayList<Image>? = ArrayList(),
        val name: String,
        val release_date: String,
        val release_date_precision: String,
        val total_tracks: Int,
        val type: String,
        val uri: String
) : Serializable

class Artists(
        val href: String,
        val items: ArrayList<ArtistItem> = ArrayList(),
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int
) : Serializable

class ArtistItem(
        val external_urls: ExternalUrls? = null,
        val genres: ArrayList<String>? = ArrayList(),
        val href: String? = null,
        val id: String? = null,
        val images: ArrayList<Image>? = ArrayList(),
        val name: String? = null,
        val popularity: Int? = null,
        val type: String? = null,
        val uri: String? = null
) : Serializable

class Track(
        val album: TrackAlbum,
        val artists: ArrayList<TrackArtist> = ArrayList(),
        val available_markets: ArrayList<String> = ArrayList(),
        val disc_number: Int,
        val duration_ms: Int,
        val explicit: Boolean,
        val external_ids: ExternalIds,
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val is_local: Boolean,
        val name: String,
        val popularity: Int,
        val preview_url: String,
        val track_number: Int,
        val type: String,
        val uri: String
) : Serializable

class ExternalUrls(
        val spotify: String
) : Serializable

class TrackArtist(
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val name: String,
        val type: String,
        val uri: String
) : Serializable

class ExternalIds(
        val isrc: String
) : Serializable

class TrackAlbum(
        val album_type: String,
        val artists: ArrayList<ArtistItem> = ArrayList(),
        val available_markets: ArrayList<String> = ArrayList(),
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val images: ArrayList<Image> = ArrayList(),
        val name: String,
        val release_date: String,
        val release_date_precision: String,
        val type: String,
        val uri: String
) : Serializable

class Image(
        val height: Int?,
        val url: String,
        val width: Int?
) : Serializable
