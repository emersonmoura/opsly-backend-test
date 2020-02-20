package com.opsly.test.opslytest

import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

class OpslyTestApplicationTests : StringSpec() {

    override fun listeners() = listOf(SpringListener)

}