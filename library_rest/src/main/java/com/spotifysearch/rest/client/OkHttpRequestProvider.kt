package com.spotifysearch.rest.client

import android.net.Uri
import android.util.Log
import com.spotifysearch.rest.exceptions.RequestFormatError
import okhttp3.Request
import okhttp3.RequestBody
import java.net.MalformedURLException
import javax.inject.Singleton

/*
 * Base provider/builder for OkHttp Requests, given an url, body and params
 */
@Singleton
open class OkHttpRequestProvider {

    internal open fun provideRequest(baseUrl: String,
                                method: RequestMethod,
                                body: RequestBody? = null,
                                vararg params: RequestParam): Request {
        val finalUrl = applyParametersToUrl(baseUrl, *params)
        val requestBuilder = applyHttpMethod(Request.Builder().url(finalUrl), method, body)
        applyHeaders(requestBuilder, *params)
        return requestBuilder.build()
    }

    /*
     * Parse query/path parameters
     */
    private fun applyParametersToUrl(baseUrl: String,
                                     vararg params: RequestParam): String {
        if (baseUrl.isEmpty()) throw MalformedURLException("Url for request can't be empty")

        val uriBuilder = Uri.parse(baseUrl).buildUpon()
        params.forEach {
            when (it.type) {

                /* ?name=value */
                RequestParamType.QUERY -> {
                    uriBuilder.appendQueryParameter(it.name, it.value)
                }

                /* /name/value (second is optional) */
                RequestParamType.PATH -> {
                    uriBuilder.appendPath(it.name)
                    it.value?.let { value -> uriBuilder.appendPath(value) }
                }
            }
        }
        return uriBuilder.build().toString()
    }

    private fun applyHeaders(requestBuilder: Request.Builder,
                             vararg params: RequestParam): Request.Builder {
        params.filter { it.type == RequestParamType.HEADER }.forEach {
            requestBuilder.addHeader(it.name, it.value ?: "")
        }
        return requestBuilder
    }

    private fun applyHttpMethod(requestBuilder: Request.Builder,
                                method: RequestMethod,
                                body: RequestBody? = null): Request.Builder {
        when (method) {
            RequestMethod.GET -> {
                if (body != null) Log.e(TAG, "GET request. Body will be ignored")
                requestBuilder.get()
            }
            RequestMethod.DELETE -> {
                requestBuilder.delete(body)
            }
            RequestMethod.POST -> {
                if (body == null) {
                    throw RequestFormatError("POST requests need to have non-null body")
                } else {
                    requestBuilder.post(body)
                }
            }
            RequestMethod.PUT -> {
                if (body == null) {
                    throw RequestFormatError("PUT requests need to have non-null body")
                } else {
                    requestBuilder.put(body)
                }
            }
            RequestMethod.PATCH -> {
                if (body == null) {
                    throw RequestFormatError("PATCH requests need to have non-null body")
                } else {
                    requestBuilder.patch(body)
                }
            }
        }
        return requestBuilder
    }

    companion object {

        private const val TAG = "RequestGenerator"

        val instance: OkHttpRequestProvider by lazy { OkHttpRequestProvider() }
    }
}

class RequestParam(val type: RequestParamType, val name: String, val value: String? = null)

enum class RequestParamType {
    QUERY,
    PATH,
    HEADER
}

enum class RequestMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH
}