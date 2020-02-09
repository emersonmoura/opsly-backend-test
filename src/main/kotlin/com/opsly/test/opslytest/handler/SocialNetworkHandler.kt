package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.SocialEventsResult
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class SocialNetworkHandler(val twitterEventHandler: TwitterEventHandler,
                           val facebookEventHandler: FacebookEventHandler,
                           val instagramEventHandler: InstagramEventHandler) {

    fun findEvents(): Flux<SocialEventsResult> {
        return Flux.just(SocialEventsResult())
    }

}