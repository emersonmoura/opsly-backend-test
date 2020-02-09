package com.opsly.test.opslytest.handler

import io.kotlintest.Spec
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import okhttp3.mockwebserver.MockWebServer
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialNetworkHandlerTest : StringSpec() {

    companion object {
        const val MAIN_URL = "http://localhost:%s"
    }
    override fun listeners() = listOf(SpringListener)

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

}