package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.FacebookEvent
import com.opsly.test.opslytest.model.SocialEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class FacebookEventHandler(@Value("\${social-networks.facebook}") var facebookUrl: String) : SocialEventHandler {

    override fun <SocialEvent> findEvents(): Flux<SocialEvent> {
        return WebClient.create()
                .get()
                .uri(facebookUrl)
                .retrieve()
                .bodyToFlux(FacebookEvent::class.java)
                .onErrorReturn(FacebookEvent()) as Flux<SocialEvent>
    }
}