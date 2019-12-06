package com.spotifysearch.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.spotifysearch.util.MethodUtils.trySilent
import io.reactivex.subjects.PublishSubject

object NetworkConnectionUtils {

    fun isNetworkConnected(context: Context?): Boolean {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (manager != null) {
            val activeNetwork = manager.activeNetwork
            if (activeNetwork != null) {
                val capabilities = manager.getNetworkCapabilities(activeNetwork)
                capabilities?.let {
                    return it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || it.hasTransport(
                            NetworkCapabilities.TRANSPORT_WIFI
                    )
                }
            }
        }
        return false
    }
}

object ConnectionStateMonitor : ConnectivityManager.NetworkCallback(), BaseConnectionStateMonitor {

    private val dispatcher: RxNetworkStateChangeDispatcher = RxNetworkStateChangeDispatcher()

    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

    override fun enable(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun disable(context: Context) {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        trySilent { connectivityManager.unregisterNetworkCallback(this) }
    }

    override fun onAvailable(network: Network) {
        dispatcher.setConnectionStatusChanged(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        dispatcher.setConnectionStatusChanged(false)
    }

    override fun onUnavailable() {
        super.onUnavailable()
        dispatcher.setConnectionStatusChanged(false)
    }
}

class RxNetworkStateChangeDispatcher {

    fun setConnectionStatusChanged(isNetworkConnected: Boolean) {
        networkStateObservable.onNext(ConnectionState(isNetworkConnected))
    }

    companion object {

        val networkStateObservable = PublishSubject.create<ConnectionState>()
    }
}

interface BaseConnectionStateMonitor {

    fun enable(context: Context)

    fun disable(context: Context)
}

class ConnectionState(
        val isConnected: Boolean?

//expand with field stating wifi vs. mobile?
)
