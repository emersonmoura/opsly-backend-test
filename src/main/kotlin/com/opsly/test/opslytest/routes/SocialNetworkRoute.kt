package com.opsly.test.opslytest.routes

import com.opsly.test.opslytest.handler.SocialNetworkHandler
import com.opsly.test.opslytest.model.SocialEventsResult
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class SocialNetworkRoute(var socialNetworkHandler: SocialNetworkHandler) {

    @Bean
    fun route() = router {
        GET("/") { ServerResponse.ok().body(socialNetworkHandler.findEvents(), SocialEventsResult::class.java) }
    }
}