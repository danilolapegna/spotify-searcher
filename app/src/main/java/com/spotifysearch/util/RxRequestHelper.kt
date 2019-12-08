package com.spotifysearch.util

import android.content.Context
import android.widget.Toast
import com.spotifysearch.R
import com.spotifysearch.util.MethodUtils.postOnMainThread
import com.spotifysearch.util.NetworkConnectionUtils.isNetworkConnected
import com.spotifysearch.util.RxNetworkStateChangeDispatcher.Companion.networkStateObservable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object RxRequestHelper {

    fun <T> executeRequest(request: Single<T>,
                           requestListener: RxRequestListener<T>,
                           context: Context,
                           delayUntilNetworkAvailable: Boolean = false) {

        val finalRequest: Single<T>

        if (delayUntilNetworkAvailable && !isNetworkConnected(context)) {
            postOnMainThread { Toast.makeText(context, R.string.will_execute_when_network_is_back, Toast.LENGTH_LONG).show() }
            finalRequest = networkStateObservable
                    .takeUntil { it.isConnected == true }
                    .singleOrError()
                    .flatMap { request }
        } else {
            finalRequest = request
        }
        executeInternal(finalRequest, requestListener)
    }

    private fun <T> executeInternal(request: Single<T>,
                                    requestListener: RxRequestListener<T>) {
        request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wrapIntoObserver(requestListener))
    }

    private fun <T> wrapIntoObserver(requestListener: RxRequestListener<T>): SingleObserver<T> {
        return object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                requestListener.onRequestSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                requestListener.onRequestStart(d)
            }

            override fun onError(e: Throwable) {
                requestListener.onRequestError(e)
            }
        }
    }

    abstract class RxRequestListener<T> {

        open fun onRequestError(throwable: Throwable) {}

        open fun onRequestStart(d: Disposable) {}

        open fun onRequestSuccess(response: T) {}
    }

}