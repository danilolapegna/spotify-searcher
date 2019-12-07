package w.com.data

import com.spotifysearch.rest.client.OkHttpRequestExecutor
import com.spotifysearch.rest.client.RequestMethod
import com.spotifysearch.rest.di.DaggerOkHttpRequestExecutorComponent
import com.spotifysearch.rest.di.TestOkHttpRequestExecutorModule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import w.com.data.di.TestMocks.mockResponse
import w.com.data.di.TestMocks.mockUrl


/**
 * Test that the OkHttpRequestExecutor still returns the response, "despite" having
 * BaseOkhttpClient and RequestProvider are mocked
 */
@RunWith(RobolectricTestRunner::class)
class OkHttpRequestExecutorTest {

    private val executor: OkHttpRequestExecutor = OkHttpRequestExecutor.instance

    @Before
    fun setup() {
        DaggerOkHttpRequestExecutorComponent
                .builder()
                .okHttpRequestExecutorModule(TestOkHttpRequestExecutorModule())
                .build()
                .inject(executor)
    }

    @Test
    fun testResponse() {
        assertEquals(executor.executeOkHttpRequest(
                mockUrl,
                RequestMethod.GET),
                mockResponse)
    }
}