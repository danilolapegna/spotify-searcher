package com.spotifysearch.rest.client

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

/*
 * TODO: Add more in case you need to expand to different MediaTypes
 */
enum class BodyType(val mediaType: MediaType?) {
    JSON(MediaType.parse("application/json; charset=utf-8")) {
        override fun convert(body: Any, gson: Gson): RequestBody {
            val jsonStringBody = gson.toJson(body)
            return RequestBody.create(mediaType, jsonStringBody)
        }
    };

    abstract fun convert(body: Any, gson: Gson): RequestBody
}