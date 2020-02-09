package com.opsly.test.opslytest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class OpslyTestApplication

fun main(args: Array<String>) {
	runApplication<OpslyTestApplication>(*args)
}
