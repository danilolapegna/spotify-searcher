package w.com.data

import com.spotifysearch.rest.RxApiClient
import com.spotifysearch.rest.di.DaggerRxApiClientComponent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import w.com.data.di.TestMocks
import w.com.data.di.TestMocks.mockRxResponse
import w.com.data.di.TestMocks.mockUrl
import w.com.data.di.TestRxApiClientModule


/**
 * Test that the RxApiClient still returns the expected response, "despite" having
 * a mocked executor
 */
@RunWith(RobolectricTestRunner::class)
class RxApiClientTest {

    private val apiClient: RxApiClient = RxApiClient.Builder().baseUrl("").build()

    @Before
    fun setup() {
        DaggerRxApiClientComponent
                .builder()
                .rxApiClientModule(TestRxApiClientModule(arrayListOf()))
                .build()
                .inject(apiClient)
    }

    @Test
    fun testResponse() {
        apiClient.getRequest(TestMocks.MockResponseClass::class.java, mockUrl)
                .test()
                .assertResult(mockRxResponse)

    }
}