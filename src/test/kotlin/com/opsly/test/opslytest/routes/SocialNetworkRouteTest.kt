package com.opsly.test.opslytest.routes

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.opsly.test.opslytest.handler.SocialNetworkHandler
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class SocialNetworkRouteTest : StringSpec() {

    override fun listeners() = listOf(SpringListener)

    init {
        "it should return a valid json with its values"{
            val list : Map<String, List<String?>> = listOf("twitter" to listOf("tweets1", "tweets2"), "facebook" to listOf("statuses"), "instagram" to listOf("photos")).toMap()
            val expected = Mono.just(list)
            val client = WebTestClient.bindToRouterFunction(SocialNetworkRoute(createMock(expected)).route()).build()
            client.get()
                    .uri("/")
                    .exchange()
                    .expectBody()
                    .json("{\"twitter\":[\"tweets1\", \"tweets2\"],\"facebook\":[\"statuses\"],\"instagram\":[\"photos\"]}")
        }
    }

    private fun createMock(events: Mono<Map<String, List<String?>>>): SocialNetworkHandler {
        return mock {
            on { findEvents() } doReturn events
        }
    }
}