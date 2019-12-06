package com.spotifysearch.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

object SpannableUtil {

    fun makeClickable(string: CharSequence?, action: () -> Any?): CharSequence? {
        return setSpan(sanitiseAsSpannable(string), object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                action.invoke()
            }
        }, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    fun setBold(string: CharSequence?, context: Context?, start: Int = 0, end: Int? = string?.length): Spannable {
        return setSpan(sanitiseAsSpannable(string), CustomTypefaceSpan("", TypefaceManager.getBoldTypeface(context)), Spannable.SPAN_INCLUSIVE_EXCLUSIVE, start, end)
    }

    fun setNormal(string: CharSequence?, context: Context?, start: Int = 0, end: Int? = string?.length): Spannable {
        return setSpan(sanitiseAsSpannable(string), CustomTypefaceSpan("", TypefaceManager.getRegularTypeface(context)), Spannable.SPAN_INCLUSIVE_EXCLUSIVE, start, end)
    }

    /* Utility function to make sure that the charsequence is consistent and can be casted to SpannableString
    * safely */
    private fun sanitiseAsSpannable(charSequence: CharSequence?): Spannable {
        var validated = charSequence
        if (validated == null) {
            validated = ""
        }
        if (validated is Spannable) {
            return validated
        } else {
            return SpannableString(validated)
        }
    }

    private fun setSpan(charSequence: CharSequence, span: Any, exclusiveInclusive: Int, start: Int = 0, end: Int? = charSequence.length): Spannable {
        val spannableString = sanitiseAsSpannable(charSequence)
        val actualEnd: Int = end ?: spannableString.length
        spannableString.setSpan(span, start, actualEnd, exclusiveInclusive)
        return spannableString
    }
}