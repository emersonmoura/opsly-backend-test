package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.FacebookEvent
import com.opsly.test.opslytest.model.InstagramEvent
import com.opsly.test.opslytest.model.SocialEventsResult
import com.opsly.test.opslytest.model.TwitterEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class SocialNetworkHandler {

    fun findEvents(baseUrl: String): Flux<SocialEventsResult> {
        return Flux.just(SocialEventsResult())
    }

    fun findTwitterEvents(baseUrl: String): Flux<TwitterEvent> {
        return WebClient.create()
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(TwitterEvent::class.java)
    }

    fun findFacebookEvents(baseUrl: String): Flux<FacebookEvent> {
        return WebClient.create()
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(FacebookEvent::class.java)
    }

    fun findInstagramEvents(baseUrl: String): Flux<InstagramEvent> {
        return WebClient.create()
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(InstagramEvent::class.java)
    }
}