package com.spotifysearch.util

import java.util.Date

object TimeUtils {

    const val ONE_SECOND_MS: Long = 1000
    const val ONE_MINUTE_MS = ONE_SECOND_MS * 60
    const val ONE_HOUR_MS = ONE_MINUTE_MS * 60
    const val ONE_DAY_MS = ONE_HOUR_MS * 24

    fun getCurrentDate() = Date(System.currentTimeMillis())

    fun timeIsOlderThan(time: Long?, olderThanMs: Long): Boolean {
        if (time == null) return false
        return (System.currentTimeMillis() - time) > olderThanMs
    }
}