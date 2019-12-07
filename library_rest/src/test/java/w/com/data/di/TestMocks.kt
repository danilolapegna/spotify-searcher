package w.com.data.di

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

object TestMocks {

    const val mockUrl = "http://mocking-everything"

    val mockRequest: Request = Request.Builder().url(mockUrl).build()

    val mockResponse: Response = Response.Builder()
            .code(HttpURLConnection.HTTP_ACCEPTED)
            .message("So happily mocked!")
            .protocol(Protocol.HTTP_2)
            .request(mockRequest).build()

    val mockCall: Call
        get() = object : Call {
            override fun enqueue(responseCallback: Callback) {}

            override fun isExecuted(): Boolean = true

            override fun clone(): Call {
                return this
            }

            override fun isCanceled(): Boolean = false

            override fun cancel() {}

            override fun request(): Request {
                return mockRequest
            }

            override fun execute(): Response {
                return mockResponse
            }
        }

    private const val mockContentString = "This was excellently mocked!"

    val mockRxResponse = MockResponseClass(mockContentString)

    class MockResponseClass(val contentString: String)

}