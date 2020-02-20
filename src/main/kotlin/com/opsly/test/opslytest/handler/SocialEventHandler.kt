package com.opsly.test.opslytest.handler

import reactor.core.publisher.Flux

interface SocialEventHandler {
    fun <SocialEvent> findEvents(): Flux<SocialEvent>
}