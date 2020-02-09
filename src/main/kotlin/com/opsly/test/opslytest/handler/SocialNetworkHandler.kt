package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.FacebookEvent
import com.opsly.test.opslytest.model.InstagramEvent
import com.opsly.test.opslytest.model.SocialEventsResult
import com.opsly.test.opslytest.model.TwitterEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class SocialNetworkHandler(@Value("\${social-networks.twitter}") var twitterUrl: String,
                           @Value("\${social-networks.facebook}") var facebookUrl: String,
                           @Value("\${social-networks.instagram}") var instagramUrl: String) {

    fun findEvents(baseUrl: String): Flux<SocialEventsResult> {
        return Flux.just(SocialEventsResult())
    }

    fun findTwitterEvents(): Flux<TwitterEvent> {
        return WebClient.create()
                .get()
                .uri(twitterUrl)
                .retrieve()
                .bodyToFlux(TwitterEvent::class.java)
    }

    fun findFacebookEvents(): Flux<FacebookEvent> {
        return WebClient.create()
                .get()
                .uri(facebookUrl)
                .retrieve()
                .bodyToFlux(FacebookEvent::class.java)
    }

    fun findInstagramEvents(): Flux<InstagramEvent> {
        return WebClient.create()
                .get()
                .uri(instagramUrl)
                .retrieve()
                .bodyToFlux(InstagramEvent::class.java)
    }
}