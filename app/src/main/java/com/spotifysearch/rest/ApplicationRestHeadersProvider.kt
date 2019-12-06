package com.spotifysearch.rest

import android.content.Context
import com.spotifysearch.SharedPreferences


class ApplicationRestHeadersProvider(val context: Context) {

    fun getContentTypeHeader(): RestHeader = object : RestHeader {
        override fun getHeaderName(): String = "Content-Type"

        override fun getHeaderValue(): String = "application/json"
    }

    fun getAuthHeader() = AuthorizationHeader(context)

    class AuthorizationHeader(val context: Context) : RestHeader {
        override fun getHeaderName(): String = "Authorization"

        override fun getHeaderValue(): String = SharedPreferences(context).getAuthToken()
    }

}
