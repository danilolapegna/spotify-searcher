package com.spotifysearch.util

import android.os.Handler
import android.os.Looper

object MethodUtils {

    /*
     * A kotlin infix helper allowing us to try a method "safely" without crashing or logging any exception.
     *
     * This is not making distinctions or logging anything so USE WITH EXTREME CAUTION!
     */
    infix fun <T> trySilent(method: () -> T?): T? {
        try {
            return method()
        } catch (e: Exception) {
            return null
        }
    }

    /*
     * A kotlin infix helper, trying to force a method on UI thread
     * whatever is the caller.
     *
     * Given the wrong methods may crash (ex. NetworkOnMainThreadException), so use with caution, too
     */
    infix fun postOnMainThread(method: () -> Any?) {
        Handler(Looper.getMainLooper()).post { method.invoke() }
    }
}
