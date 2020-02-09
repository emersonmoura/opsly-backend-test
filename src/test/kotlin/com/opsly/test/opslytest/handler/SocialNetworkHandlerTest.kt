package com.opsly.test.opslytest.handler

import io.kotlintest.Spec
import io.kotlintest.TestCase
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

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        mockServer.start()
    }

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        val baseUrl = String.format(MAIN_URL, mockServer.port)
        socialNetworkHandler.facebookUrl = baseUrl
        socialNetworkHandler.twitterUrl = baseUrl
        socialNetworkHandler.instagramUrl = baseUrl
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        mockServer.shutdown()
    }

    init {

        //TWITTER EVENTS

        "given a twitter json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents().collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a twitter json item with username should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents().collectList().block()

            result?.firstOrNull()?.username shouldNotBe null
        }

        "given a twitter json item with tweet should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = socialNetworkHandler.findTwitterEvents().collectList().block()

            result?.firstOrNull()?.tweet shouldNotBe null
        }

        "it should return error with invalid twitter json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                socialNetworkHandler.findTwitterEvents().collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }

        //FACEBOOK EVENTS

        "given a facebook json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents().collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a facebook json item with name should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents().collectList().block()

            result?.firstOrNull()?.name shouldNotBe null
        }

        "given a facebook json item with status should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = socialNetworkHandler.findFacebookEvents().collectList().block()

            result?.firstOrNull()?.status shouldNotBe null
        }

        "it should return error with invalid facebook json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                socialNetworkHandler.findFacebookEvents().collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }

        //INSTAGRAM EVENTS

        "given a instagram json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = socialNetworkHandler.findInstagramEvents().collectList().block()

            result?.shouldHaveSize(5)
        }

        "given a instagram json item with username should compute the value" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = socialNetworkHandler.findInstagramEvents().collectList().block()

            result?.firstOrNull()?.username shouldNotBe null
        }

        "given a instagram json item with picture should compute the value" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = socialNetworkHandler.findInstagramEvents().collectList().block()

            result?.firstOrNull()?.picture shouldNotBe null
        }

        "it should return error with invalid instagram json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                socialNetworkHandler.findInstagramEvents().collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }
    }

    private fun twitterBody(): String {
        return """
            [
              {
                "username": "@GuyEndoreKaiser",
                "tweet": "If you live to be 100, you should make up some fake reason why, just to mess with people... like claim you ate a pinecone every single day."
              },
              {
                "username": "@mikeleffingwell",
                "tweet": "STOP TELLING ME YOUR NEWBORN'S WEIGHT AND LENGTH I DON'T KNOW WHAT TO DO WITH THAT INFORMATION."
              }
            ]""".trimIndent()
    }

    private fun facebookBody(): String {
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
            ]""".trimIndent()
    }

    private fun instagramBody(): String {
        return """
              [
                  {
                    "username": "hipster1",
                    "picture": "food"
                  },
                  {
                    "username": "hipster2",
                    "picture": "coffee"
                  },
                  {
                    "username": "hipster3",
                    "picture": "coffee"
                  },
                  {
                    "username": "hipster4",
                    "picture": "food"
                  },
                  {
                    "username": "hipster5",
                    "picture": "this one is of a cat"
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