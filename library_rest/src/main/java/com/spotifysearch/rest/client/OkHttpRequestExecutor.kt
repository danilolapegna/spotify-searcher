package com.spotifysearch.rest.client
import android.support.annotation.WorkerThread
import com.google.gson.Gson
import com.spotifysearch.rest.BaseRestRequestExecutor
import com.spotifysearch.rest.RestHeader
import com.spotifysearch.rest.di.DaggerOkHttpRequestExecutorComponent
import com.spotifysearch.rest.di.OkHttpRequestExecutorModule
import com.spotifysearch.rest.exceptions.HttpException
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

/*
 *
 * A possible implementation for BaseRestRequestExecutor, using OkHttp
 *
 * Async methods to invoke GET/POST/PUT/PATCH/DELETE methods on given url, with given params, by
 * using an OkHttpClient
 *
 * Only invoke from worker thread and, if possible, never directly, but through an ApiClient
 */
@Singleton
open class OkHttpRequestExecutor(baseHeaders: List<RestHeader>? = null) : BaseRestRequestExecutor {

    @Inject
    lateinit var okHttpRequestProvider: OkHttpRequestProvider

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var gson: Gson


    init {
        DaggerOkHttpRequestExecutorComponent
                .builder()
                .okHttpRequestExecutorModule(OkHttpRequestExecutorModule(baseHeaders))
                .build()
                .inject(this)
    }

    /*
     * Run to get final, de-serialised response
     */
    @Throws(HttpException::class)
    @WorkerThread
    override fun <T> executeAndParseRestRequest(responseClass: Class<T>,
                                                baseUrl: String,
                                                requestMethod: RequestMethod,
                                                body: Any?,
                                                bodyType: BodyType,
                                                vararg params: RequestParam): T {
        val okHttpBody = executeOkHttpRequest(baseUrl, requestMethod, body, bodyType, *params)
        return gson.fromJson(parseBody(okHttpBody)?.string(), responseClass)
    }

    /*
     * Run to get Okhttp request only
     */
    fun executeOkHttpRequest(baseUrl: String,
                             requestMethod: RequestMethod,
                             body: Any? = null,
                             bodyType: BodyType = BodyType.JSON,
                             vararg params: RequestParam): Response {
        val requestBody = if (body != null) {
            bodyType.convert(body, gson)
        } else null

        val request = okHttpRequestProvider.provideRequest(baseUrl, requestMethod, requestBody, *params)
        return okHttpClient.newCall(request).execute()
    }

    private fun parseBody(response: Response): ResponseBody? {
        return if (response.isSuccessful) {
            response.body()
        } else {
            throw HttpException(response.code(), response.message(), response.body())
        }
    }

    companion object {

        val instance = OkHttpRequestExecutor()
    }
}

