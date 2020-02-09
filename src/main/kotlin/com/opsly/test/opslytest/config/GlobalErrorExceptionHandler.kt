package com.opsly.test.opslytest.config

import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
@Order(Integer.MIN_VALUE)
class GlobalErrorExceptionHandler(g:  org.springframework.boot.web.reactive.error.ErrorAttributes?,
                                  applicationContext: ApplicationContext, serverCodecConfigurer: ServerCodecConfigurer)
    : AbstractErrorWebExceptionHandler(g, ResourceProperties(), applicationContext) {

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: org.springframework.boot.web.reactive.error.ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), HandlerFunction<ServerResponse> { renderErrorResponse(it) })
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse?> {
        val errorPropertiesMap = getErrorAttributes(request, false)
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap))
    }
}