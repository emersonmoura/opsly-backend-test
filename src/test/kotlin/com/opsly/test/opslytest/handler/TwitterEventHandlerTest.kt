package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.ResponseMocker.createMockedResponse
import com.opsly.test.opslytest.model.TwitterEvent
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TwitterEventHandlerTest : StringSpec() {

    companion object {
        const val MAIN_URL = "http://localhost:%s"
    }
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var twitterEventHandler: TwitterEventHandler

    val mockServer = MockWebServer()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        mockServer.start()
    }

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        val baseUrl = String.format(MAIN_URL, mockServer.port)
        twitterEventHandler.twitterUrl = baseUrl
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        mockServer.shutdown()
    }

    init {
        "given a twitter json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = twitterEventHandler.findEvents<TwitterEvent>().collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a twitter json item with username should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = twitterEventHandler.findEvents<TwitterEvent>().collectList().block()

            result?.firstOrNull()?.username shouldNotBe null
        }

        "given a twitter json item with tweet should compute the value" {
            mockServer.enqueue(createMockedResponse(200, twitterBody()))

            val result = twitterEventHandler.findEvents<TwitterEvent>().collectList().block()

            result?.firstOrNull()?.tweet shouldNotBe null
        }

        "given an http error it should return an empty object" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val result = twitterEventHandler.findEvents<TwitterEvent>().collectList().block()

            result?.shouldHaveSize(1)
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
}