package com.opsly.test.opslytest.handler

import com.opsly.test.opslytest.model.SocialEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class SocialNetworkHandler(@Autowired var socialEventHandlers: MutableList<SocialEventHandler>){

    fun findEvents(): Mono<Map<String, List<String?>>> {
        return Flux.fromIterable(socialEventHandlers)
                .map { it.findEvents<SocialEvent>() }
                .flatMap { it.collectList() }
                .map {
                    it.filter { it.hasInformation() }.groupBy({ it.networkName() },{ it.information() })
                }.reduce(emptyMap()) {
                    result, current -> result.plus(current)
                }
    }

    private fun emptyMap() = mapOf<String, List<String?>>()
}