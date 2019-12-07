package w.com.data.di

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.spotifysearch.rest.BaseRestRequestExecutor
import com.spotifysearch.rest.RestHeader
import com.spotifysearch.rest.client.RequestMethod
import com.spotifysearch.rest.di.RxApiClientModule
import dagger.Module
import dagger.Provides
import w.com.data.di.TestMocks.mockRxResponse
import w.com.data.di.TestMocks.mockUrl
import javax.inject.Singleton

@Module
open class TestRxApiClientModule(baseHeaders: List<RestHeader>?) : RxApiClientModule(baseHeaders) {

    @Provides
    @Singleton
    override fun provideExecutor(): BaseRestRequestExecutor {
        return mock {
            on {
                executeAndParseRestRequest(TestMocks.MockResponseClass::class.java,
                        mockUrl,
                        RequestMethod.GET,
                        null)
            } doReturn mockRxResponse
        }
    }

}