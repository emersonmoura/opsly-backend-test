package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.ResponseMocker.createMockedResponse
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class InstagramEventHandlerTest  : StringSpec() {

    companion object {
        const val MAIN_URL = "http://localhost:%s"
    }
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var instagramEventHandler: InstagramEventHandler

    val mockServer = MockWebServer()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        mockServer.start()
    }

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        val baseUrl = String.format(MAIN_URL, mockServer.port)
        instagramEventHandler.instagramUrl = baseUrl
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        mockServer.shutdown()
    }

    init {

        "given a instagram json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = instagramEventHandler.findInstagramEvents().collectList().block()

            result?.shouldHaveSize(5)
        }

        "given a instagram json item with username should compute the value" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = instagramEventHandler.findInstagramEvents().collectList().block()

            result?.firstOrNull()?.username shouldNotBe null
        }

        "given a instagram json item with picture should compute the value" {
            mockServer.enqueue(createMockedResponse(200, instagramBody()))

            val result = instagramEventHandler.findInstagramEvents().collectList().block()

            result?.firstOrNull()?.picture shouldNotBe null
        }

        "given an http error it should return an empty object" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val result = instagramEventHandler.findInstagramEvents().collectList().block()

            result?.shouldHaveSize(1)
        }
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
}