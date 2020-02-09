package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.SocialEventsResult
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class SocialNetworkHandler(var twitterEventHandler: TwitterEventHandler,
                           var facebookEventHandler: FacebookEventHandler,
                           var instagramEventHandler: InstagramEventHandler,
                           val modelMapper: ModelMapper) {

    fun findEvents(): Mono<SocialEventsResult> {
        val twitter = twitterEventHandler.findTwitterEvents().collectList()
        val facebook = facebookEventHandler.findFacebookEvents().collectList()
        val instagram = instagramEventHandler.findInstagramEvents().collectList()
        return Mono.zip(twitter,facebook,instagram).map { modelMapper.map(it, SocialEventsResult::class.java) }
    }

}