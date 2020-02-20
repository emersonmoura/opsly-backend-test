package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.InstagramEvent
import com.opsly.test.opslytest.model.SocialEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class InstagramEventHandler(@Value("\${social-networks.instagram}") var instagramUrl: String) : SocialEventHandler {

    override fun <SocialEvent> findEvents(): Flux<SocialEvent> {
        return WebClient.create()
                .get()
                .uri(instagramUrl)
                .retrieve()
                .bodyToFlux(InstagramEvent::class.java)
                .onErrorReturn(InstagramEvent()) as Flux<SocialEvent>
    }
}