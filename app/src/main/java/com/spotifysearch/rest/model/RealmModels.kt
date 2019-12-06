package com.spotifysearch.rest.model

import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.Date

@RealmClass
open class RealmTrack : RealmModel {

    @PrimaryKey
    lateinit var id: String

    lateinit var storedAt: Date

    var name: String? = null

    var albumName: String? = null

    var albumTrackNumber: Int? = null

    var albumImages: RealmList<RealmImage> = RealmList()

    var artists: RealmList<RealmArtist> = RealmList()

    var externalSpotifyUrl: String? = null

    var trackDurationMs: Long? = null


}

@RealmClass
open class RealmArtist : RealmModel {

    @PrimaryKey
    lateinit var id: String

    lateinit var storedAt: Date

    var name: String? = null

    var artistImages: RealmList<RealmImage> = RealmList()

    var externalSpotifyUrl: String? = null

    var genres: RealmList<String> = RealmList()

}

@RealmClass
open class RealmImage : RealmModel {

    @PrimaryKey
    lateinit var id: String

    lateinit var storedAt: Date

    var url: String? = null

    var height: Int? = null

    var width: Int? = null


}


