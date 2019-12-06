package com.spotifysearch

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences constructor(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy { context.getSharedPreferences(SHARED_PREFS_NAME, 0) }

    fun setAuthToken(authToken: String) {
        sharedPreferences.edit()
                .putString(SHARED_PREFS_TOKEN, "Bearer $authToken")
                .apply()
    }

    fun getAuthToken(): String = sharedPreferences.getString(SHARED_PREFS_TOKEN, "") ?: ""

    fun removeAuthToken() {
        sharedPreferences.edit()
                .remove(SHARED_PREFS_TOKEN)
                .apply()
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getString(SHARED_PREFS_TOKEN, "") != ""

    companion object {

        private const val SHARED_PREFS_NAME = "shared preferences"

        private const val SHARED_PREFS_TOKEN = "token"
    }
}