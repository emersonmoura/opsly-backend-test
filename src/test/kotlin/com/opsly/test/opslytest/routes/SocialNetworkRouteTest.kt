package com.opsly.test.opslytest.routes

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.opsly.test.opslytest.handler.SocialNetworkHandler
import com.opsly.test.opslytest.model.SocialEventsResult
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@SpringBootTest
class SocialNetworkRouteTest : StringSpec() {

    override fun listeners() = listOf(SpringListener)

    init {
        "it should return a valid json with its values"{
            val expected = Mono.just(SocialEventsResult(listOf("tweets"), listOf("statuses"), listOf("photos")))
            val client = WebTestClient.bindToRouterFunction(SocialNetworkRoute(createMock(expected)).route()).build()
            client.get()
                    .uri("/")
                    .exchange()
                    .expectBody()
                    .json("{\"twitter\":[\"tweets\"],\"facebook\":[\"statuses\"],\"instagram\":[\"photos\"]}")
        }
    }

    private fun createMock(events: Mono<SocialEventsResult> = Mono.just(SocialEventsResult())): SocialNetworkHandler {
        return mock<SocialNetworkHandler> {
            on { findEvents() } doReturn events
        }
    }
}