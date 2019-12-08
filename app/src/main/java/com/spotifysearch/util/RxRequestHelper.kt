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

    private var networkSubscription: Disposable? = null

    private var queuedExecuteRequestAction: (() -> Unit)? = null

    fun <T> executeRequest(request: Single<T>,
                           requestListener: RxRequestListener<T>,
                           context: Context,
                           queueIfNoNetwork: Boolean = false) {

        val executeRequestAction = {
            request.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(wrapIntoObserver(requestListener, context))

        }
        if (queueIfNoNetwork) {
            subscribeToNetworkEventsIfNot()
            if (isNetworkConnected(context)) {
                executeRequestAction.invoke()
            } else {
                postOnMainThread { Toast.makeText(context, R.string.will_execute_when_network_is_back, Toast.LENGTH_LONG).show() }
                queuedExecuteRequestAction = executeRequestAction
            }
        } else {
            executeRequestAction.invoke()
        }
    }

    private fun <T> wrapIntoObserver(requestListener: RxRequestListener<T>, context: Context): SingleObserver<T> {
        return object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                queuedExecuteRequestAction = null
                requestListener.onRequestSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
                requestListener.onRequestStart(d)
            }

            override fun onError(e: Throwable) {
                if (isNetworkConnected(context)) {

                    /* Remove from queue only if is not an error-related network */
                    queuedExecuteRequestAction = null
                }
                requestListener.onRequestError(e)
            }
        }
    }

    private fun subscribeToNetworkEventsIfNot() {
        if (networkSubscription == null) {
            networkSubscription = networkStateObservable.subscribe { connectionState ->
                if (connectionState.isConnected == true) {
                    executeNextRequestInQueue()
                }
            }
        }
    }

    private fun executeNextRequestInQueue() {
        queuedExecuteRequestAction?.invoke()
    }

    abstract class RxRequestListener<T> {

        open fun onRequestError(throwable: Throwable) {}

        open fun onRequestStart(d: Disposable) {}

        open fun onRequestSuccess(response: T) {}
    }

}