package com.opsly.test.opslytest.handler

import io.kotlintest.Spec
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialNetworkHandlerTest : StringSpec() {

    companion object {
        const val MAIN_URL = "http://localhost:%s"
        const val CONTENT_TYPE = "Content-type"
        const val JSON = "application/json"
    }
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var socialNetworkHandler: SocialNetworkHandler

    val mockServer = MockWebServer()

    var baseUrl = ""

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        mockServer.start()
        baseUrl = String.format(MAIN_URL, mockServer.port)
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        mockServer.shutdown()
    }

    init {

        //TWITTER EVENTS

        "given a twitter json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents(baseUrl).collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a twitter json item with username should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents(baseUrl).collectList().block()

            result?.firstOrNull()?.username shouldNotBe null
        }

        "given a twitter json item with tweet should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents(baseUrl).collectList().block()

            result?.firstOrNull()?.tweet shouldNotBe null
        }

        "it should return error with invalid json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                socialNetworkHandler.findTwitterEvents(baseUrl).collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }

        //FACEBOOK EVENTS

        "given a facebook json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents(baseUrl).collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a facebook json item with name should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents(baseUrl).collectList().block()

            result?.firstOrNull()?.name shouldNotBe null
        }

        "given a facebook json item with status should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents(baseUrl).collectList().block()

            result?.firstOrNull()?.status shouldNotBe null
        }

        "it should return error with invalid json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                socialNetworkHandler.findFacebookEvents(baseUrl).collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }
    }

    private fun twitterBody(): String {
        return """
                    [
                      {
                        "name": "Some Friend",
                        "status": "Here's some photos of my holiday. Look how much more fun I'm having than you are!"
                      },
                      {
                        "name": "Drama Pig",
                        "status": "I am in a hospital. I will not tell you anything about why I am here."
                      }
                    ]
                """.trimIndent()
    }

    private fun facebookBody(): String {
        return """
              {
                "name": "Some Friend",
                "status": "Here's some photos of my holiday. Look how much more fun I'm having than you are!"
              },
              {
                "name": "Drama Pig",
                "status": "I am in a hospital. I will not tell you anything about why I am here."
              }
            ]""".trimIndent()
    }

    private fun createMockedResponse(status: Int, body: String): MockResponse {
        val mockedResponse = MockResponse()
        mockedResponse.setResponseCode(status)
        mockedResponse.setHeader(CONTENT_TYPE, JSON)
        mockedResponse.setBody(body)
        return mockedResponse
    }

}