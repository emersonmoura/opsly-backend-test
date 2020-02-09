package com.opsly.test.opslytest.handler

import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocialNetworkHandlerTest : StringSpec() {

    override fun listeners() = listOf(SpringListener)

}