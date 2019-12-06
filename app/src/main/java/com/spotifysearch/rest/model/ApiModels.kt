package com.spotifysearch.rest.model

class SearchResponse(
        val artists: Artists? = null,
        val tracks: Tracks? = null
)

class Tracks(
        val href: String,
        val items: List<TrackItem> = ArrayList(),
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int
)

class TrackItem(
        val album: Album? = null,
        val artists: List<ArtistItem>? = ArrayList(),
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
)

class Album(
        val album_type: String,
        val artists: List<ArtistItem> = ArrayList(),
        val genres: List<String> = ArrayList(),
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val images: List<Image>? = ArrayList(),
        val name: String,
        val release_date: String,
        val release_date_precision: String,
        val total_tracks: Int,
        val type: String,
        val uri: String
)

class Artists(
        val href: String,
        val items: List<ArtistItem> = ArrayList(),
        val limit: Int,
        val next: String,
        val offset: Int,
        val previous: String,
        val total: Int
)

class ArtistItem(
        val external_urls: ExternalUrls? = null,
        val genres: List<String>? = ArrayList(),
        val href: String? = null,
        val id: String? = null,
        val images: List<Image>? = ArrayList(),
        val name: String? = null,
        val popularity: Int? = null,
        val type: String? = null,
        val uri: String? = null
)

class Track(
        val album: TrackAlbum,
        val artists: List<TrackArtist> = ArrayList(),
        val available_markets: List<String> = ArrayList(),
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
)

class ExternalUrls(
        val spotify: String
)

class TrackArtist(
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val name: String,
        val type: String,
        val uri: String
)

class ExternalIds(
        val isrc: String
)

class TrackAlbum(
        val album_type: String,
        val artists: List<ArtistItem> = ArrayList(),
        val available_markets: List<String> = ArrayList(),
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val images: List<Image> = ArrayList(),
        val name: String,
        val release_date: String,
        val release_date_precision: String,
        val type: String,
        val uri: String
)

class Image(
        val height: Int?,
        val url: String,
        val width: Int?
)
