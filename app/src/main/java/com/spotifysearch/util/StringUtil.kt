package com.spotifysearch.util

import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StringUtil {

    private const val READABLE_TRACK_TIME_FORMAT = "hh:mm:ss"

    private fun getCurrentLocale(context: Context?): Locale? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context?.resources?.configuration?.locales?.get(0)
        } else {
            return context?.resources?.configuration?.locale
        }
    }

    fun toReadableTime(context: Context?, timeInMilliSeconds: Long?) = SimpleDateFormat(READABLE_TRACK_TIME_FORMAT,
            getCurrentLocale(context))
            .format(Date(timeInMilliSeconds ?: 0L))
}