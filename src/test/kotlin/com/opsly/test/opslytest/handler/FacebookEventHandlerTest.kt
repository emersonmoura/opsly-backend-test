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
class FacebookEventHandlerTest : StringSpec() {

    companion object {
        const val MAIN_URL = "http://localhost:%s"
    }
    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var facebookEventHandler: FacebookEventHandler

    val mockServer = MockWebServer()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        mockServer.start()
    }

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        val baseUrl = String.format(MAIN_URL, mockServer.port)
        facebookEventHandler.facebookUrl = baseUrl
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        mockServer.shutdown()
    }

    init {
        "given a facebook json body with items should compute each one" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = facebookEventHandler.findFacebookEvents().collectList().block()

            result?.shouldHaveSize(2)
        }

        "given a facebook json item with name should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = facebookEventHandler.findFacebookEvents().collectList().block()

            result?.firstOrNull()?.name shouldNotBe null
        }

        "given a facebook json item with status should compute the value" {
            mockServer.enqueue(createMockedResponse(200, facebookBody()))

            val result = facebookEventHandler.findFacebookEvents().collectList().block()

            result?.firstOrNull()?.status shouldNotBe null
        }

        "it should return error with invalid facebook json" {
            val body = "I am trapped in a social media factory send help"
            mockServer.enqueue(createMockedResponse(500,body))

            val thrown: Exception = shouldThrow {
                facebookEventHandler.findFacebookEvents().collectList().block()
            }

            thrown.message shouldContain  "500 Internal Server Error from GET"
        }
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
}