package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.SocialEvent
import com.opsly.test.opslytest.model.TwitterEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class TwitterEventHandler(@Value("\${social-networks.twitter}") var twitterUrl: String) : SocialEventHandler {

    override fun <SocialEvent> findEvents(): Flux<SocialEvent> {
        return WebClient.create()
                .get()
                .uri(twitterUrl)
                .retrieve()
                .bodyToFlux(TwitterEvent::class.java)
                .onErrorReturn(TwitterEvent()) as Flux<SocialEvent>
    }
}