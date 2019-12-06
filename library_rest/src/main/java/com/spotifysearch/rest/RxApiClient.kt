package com.spotifysearch.rest

import com.google.gson.Gson
import com.spotifysearch.rest.client.BodyType
import com.spotifysearch.rest.client.RequestMethod
import com.spotifysearch.rest.client.RequestParam
import com.spotifysearch.rest.di.DaggerRxApiClientComponent
import com.spotifysearch.rest.di.RxApiClientModule
import io.reactivex.Single
import javax.inject.Inject

/*
 * An Rx-based async wrapper for BaseRestRequestExecutor. Could inject any executor, if needed.
 *
 * You can either get your Single<T> network calls from here directly, or use a RxApiClientRequestBuilder
 * for better readability
 */
class RxApiClient private constructor(
        private val baseUrl: String? = null,
        baseHeaders: List<RestHeader>? = null) {

    @Inject
    lateinit var executor: BaseRestRequestExecutor

    @Inject
    lateinit var gson: Gson

    init {
        DaggerRxApiClientComponent
                .builder()
                .rxApiClientModule(RxApiClientModule(baseHeaders))
                .build()
                .inject(this)
    }

    fun <T> getRequest(responseClass: Class<T>,
                       url: String,
                       vararg params: RequestParam): Single<T> {
        return buildSingleRequest(RequestMethod.GET, responseClass, url, params = *params)
    }

    fun <T> postRequest(responseClass: Class<T>,
                        url: String,
                        body: Any? = null,
                        bodyType: BodyType = BodyType.JSON,
                        vararg params: RequestParam): Single<T> {
        return buildSingleRequest(RequestMethod.POST, responseClass, url, body, bodyType, *params)
    }

    fun <T> putRequest(responseClass: Class<T>,
                       url: String,
                       body: Any? = null,
                       bodyType: BodyType = BodyType.JSON,
                       vararg params: RequestParam): Single<T> {
        return buildSingleRequest(RequestMethod.PUT, responseClass, url, body, bodyType, *params)
    }

    fun <T> patchRequest(responseClass: Class<T>,
                         url: String,
                         body: Any? = null,
                         bodyType: BodyType = BodyType.JSON,
                         vararg params: RequestParam): Single<T> {
        return buildSingleRequest(RequestMethod.PATCH, responseClass, url, body, bodyType, *params)
    }

    fun <T> deleteRequest(responseClass: Class<T>,
                          url: String,
                          body: Any? = null,
                          bodyType: BodyType = BodyType.JSON,
                          vararg params: RequestParam): Single<T> {
        return buildSingleRequest(RequestMethod.DELETE, responseClass, url, body, bodyType, *params)
    }

    private fun <T> buildSingleRequest(method: RequestMethod,
                                       responseClass: Class<T>,
                                       additionalUrl: String,
                                       body: Any? = null,
                                       bodyType: BodyType = BodyType.JSON,
                                       vararg params: RequestParam): Single<T> {

        return Single.create { subscriber ->
            val finalUrl = "$baseUrl$additionalUrl"
            try {
                val result = { executor.executeRestRequest(responseClass, finalUrl, method, body, bodyType, *params) }.invoke()
                subscriber.onSuccess(result)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    class Builder {

        private var baseUrl: String? = null

        private var baseHeaders: ArrayList<RestHeader>? = null

        fun baseUrl(baseUrl: String?): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun headers(restHeaders: List<RestHeader>): Builder {
            shouldInitHeaders()
            restHeaders.forEach { header ->
                baseHeaders?.add(header)
            }
            return this
        }

        fun header(restHeader: RestHeader): Builder {
            shouldInitHeaders()
            baseHeaders?.add(restHeader)
            return this
        }

        fun header(name: String, value: String): Builder {
            shouldInitHeaders()
            baseHeaders?.add(buildSimpleHeader(name, value))
            return this
        }

        private fun shouldInitHeaders() {
            if (baseHeaders == null) baseHeaders = ArrayList()
        }

        private fun buildSimpleHeader(name: String, value: String) = object : RestHeader {
            override fun getHeaderName(): String = name

            override fun getHeaderValue(): String = value
        }

        fun build(): RxApiClient = RxApiClient(baseUrl, baseHeaders)
    }

    companion object {

    }
}