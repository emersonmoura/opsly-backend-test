package com.opsly.test.opslytest.routes

import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialNetworkRouteTest : StringSpec() {

    override fun listeners() = listOf(SpringListener)
}