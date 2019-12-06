package com.spotifysearch.util;

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import com.spotifysearch.R

object TypefaceManager {

    fun getBoldTypeface(context: Context?): Typeface = getTypeface(context, Type.PROXIMA_SOFT_BOLD)

    fun getRegularTypeface(context: Context?): Typeface = getTypeface(context, Type.PROXIMA_SOFT_REGULAR)

    private fun getTypeface(context: Context?, type: Type): Typeface {
        return context?.let { ResourcesCompat.getFont(it, type.fontResId) }
                ?: Typeface.DEFAULT
    }

    enum class Type constructor(val fontResId: Int) {
        PROXIMA_SOFT_REGULAR(R.font.proximanova),
        PROXIMA_SOFT_BOLD(R.font.proximanova_w_max);
    }
}