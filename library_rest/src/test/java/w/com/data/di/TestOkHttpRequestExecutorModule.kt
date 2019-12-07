package com.spotifysearch.rest.di

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.spotifysearch.rest.client.OkHttpRequestProvider
import com.spotifysearch.rest.client.RequestMethod
import dagger.Module
import dagger.Provides
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import w.com.data.di.TestMocks.mockCall
import w.com.data.di.TestMocks.mockRequest
import w.com.data.di.TestMocks.mockUrl
import java.net.HttpURLConnection
import javax.inject.Singleton

@Module
class TestOkHttpRequestExecutorModule : OkHttpRequestExecutorModule() {

    /*
     * Provides a mock http client
     */
    @Provides
    @Singleton
    override fun provideBaseClient(): OkHttpClient {
        return mock {
            on { newCall(mockRequest) } doReturn mockCall
        }
    }

    @Provides
    @Singleton
    override fun provideRequestProvider(): OkHttpRequestProvider {
        return mock {
            on { provideRequest(mockUrl, RequestMethod.GET) } doReturn mockRequest
        }
    }
}