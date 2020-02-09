package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.SocialEventsResult
import com.opsly.test.opslytest.model.TwitterEvent
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class SocialNetworkHandler {

    fun findEvents(baseUrl: String): Flux<SocialEventsResult> {
        return Flux.just(SocialEventsResult())
    }

    fun findTwitterEvents(baseUrl: String): Flux<TwitterEvent> {
        return Flux.just(TwitterEvent())
    }
}