package com.spotifysearch.rest.client

import com.spotifysearch.rest.RestHeader
import okhttp3.Interceptor
import okhttp3.Response

/*
 * Defult OkHttp implementation to add our base headers, defined in the client to each call
 */
class OkHttpAddHeaderInterceptor(val headers: List<RestHeader>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (headers == null || headers.isEmpty()) {
            return chain.proceed(chain.request())
        } else {
            val request = chain.request()
            val builder = request.newBuilder()
            headers.forEach {
                if (it.getHeaderName().isNotEmpty() && it.getHeaderValue().isNotEmpty()) {
                    builder.addHeader(it.getHeaderName(), it.getHeaderValue())
                }
            }
            val updatedRequest = builder.build()
            return chain.proceed(updatedRequest)
        }
    }
}