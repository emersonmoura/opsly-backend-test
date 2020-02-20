package com.opsly.test.opslytest.routes

import com.opsly.test.opslytest.handler.SocialNetworkHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class SocialNetworkRoute(var socialNetworkHandler: SocialNetworkHandler) {

    @Bean
    fun route() = router {
        fun typeReference() = object : ParameterizedTypeReference<Map<String, List<String?>>>() {}
        GET("/") { ServerResponse.ok().body(socialNetworkHandler.findEvents(), typeReference()) }
    }
}