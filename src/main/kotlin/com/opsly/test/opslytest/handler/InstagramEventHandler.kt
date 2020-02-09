package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.InstagramEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class InstagramEventHandler(@Value("\${social-networks.instagram}") var instagramUrl: String) {

    fun findInstagramEvents(): Flux<InstagramEvent> {
        return WebClient.create()
                .get()
                .uri(instagramUrl)
                .retrieve()
                .bodyToFlux(InstagramEvent::class.java)
                .onErrorReturn(InstagramEvent())
    }
}