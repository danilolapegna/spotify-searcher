package com.spotifysearch.rest

import android.util.Log
import com.spotifysearch.rest.client.BodyType
import com.spotifysearch.rest.client.RequestMethod
import com.spotifysearch.rest.client.RequestParam
import com.spotifysearch.rest.client.RequestParamType
import com.spotifysearch.rest.util.ListUtils.buildParametersArray
import io.reactivex.Single
import java.security.InvalidParameterException

/*
 * Use a builder pattern to call RxApiClient from UI, for better usability/readability
 */

class RxApiClientRequestBuilder<T> {

    private val TAG = RxApiClientRequestBuilder::class.java.name

    var client: RxApiClient? = null
    var responseClass: Class<T>? = null
    var method: RequestMethod? = null
    var additionalUrl: String = ""
    var body: Any? = null
    var bodyType: BodyType = BodyType.JSON
    val parameters: ArrayList<RequestParam> = ArrayList()

    fun client(client: RxApiClient): RxApiClientRequestBuilder<T> {
        this.client = client
        return this
    }

    fun responseClass(responseClass: Class<T>): RxApiClientRequestBuilder<T> {
        this.responseClass = responseClass
        return this
    }

    fun method(method: RequestMethod): RxApiClientRequestBuilder<T> {
        this.method = method
        return this
    }

    fun additionalUrl(url: String): RxApiClientRequestBuilder<T> {
        this.additionalUrl = url
        return this
    }

    fun body(body: Any, bodyType: BodyType = BodyType.JSON): RxApiClientRequestBuilder<T> {
        this.body = body
        this.bodyType = bodyType
        return this
    }

    fun queryParameter(name: String, value: String): RxApiClientRequestBuilder<T> {
        parameters.add(RequestParam(RequestParamType.QUERY, name, value))
        return this
    }

    fun pathParameter(name: String, value: String): RxApiClientRequestBuilder<T> {
        parameters.add(RequestParam(RequestParamType.PATH, name, value))
        return this
    }

    fun header(name: String, value: String): RxApiClientRequestBuilder<T> {
        parameters.add(RequestParam(RequestParamType.HEADER, name, value))
        return this
    }

    fun build(): Single<T> {
        if (responseClass == null) {
            throw InvalidParameterException("You need to define a response class")
        } else {
            client?.let { client ->
                return when (method) {
                    null -> {
                        Log.d(TAG, "No explicit HTTP method defined. Defaulting to GET")
                        client.getRequest(responseClass!!, additionalUrl, *buildParametersArray(parameters))
                    }
                    RequestMethod.GET -> {
                        client.getRequest(responseClass!!, additionalUrl, *buildParametersArray(parameters))
                    }
                    RequestMethod.POST -> {
                        client.postRequest(responseClass!!, additionalUrl, body, bodyType, *buildParametersArray(parameters))
                    }
                    RequestMethod.PUT -> {
                        client.putRequest(responseClass!!, additionalUrl, body, bodyType, *buildParametersArray(parameters))
                    }
                    RequestMethod.DELETE -> {
                        client.deleteRequest(responseClass!!, additionalUrl, body, bodyType, *buildParametersArray(parameters))
                    }
                    RequestMethod.PATCH -> {
                        client.patchRequest(responseClass!!, additionalUrl, body, bodyType, *buildParametersArray(parameters))
                    }
                }
            }
            throw InvalidParameterException("You need to define a non-null client")
        }
    }
}