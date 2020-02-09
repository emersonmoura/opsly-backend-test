package com.opsly.test.opslytest

import okhttp3.mockwebserver.MockResponse

object ResponseMocker {

    const val CONTENT_TYPE = "Content-type"
    const val JSON = "application/json"

    fun createMockedResponse(status: Int, body: String): MockResponse {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(status)
        mockedResponse.setHeader(CONTENT_TYPE, JSON)
        mockedResponse.setBody(body)
        return mockedResponse
    }
}