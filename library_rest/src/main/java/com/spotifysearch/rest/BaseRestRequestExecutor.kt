package com.spotifysearch.rest

import android.support.annotation.WorkerThread
import com.spotifysearch.rest.client.BodyType
import com.spotifysearch.rest.client.RequestMethod
import com.spotifysearch.rest.client.RequestParam

/*
 * An abstract entity whose contract is based on executing RESTful requests
 *
 * Default implementation in this library is via OkHttp
 */
interface BaseRestRequestExecutor {

    @WorkerThread
    fun <T> executeRestRequest(responseClass: Class<T>,
                               baseUrl: String,
                               requestMethod: RequestMethod,
                               body: Any?,
                               bodyType: BodyType = BodyType.JSON,
                               vararg params: RequestParam): T
}