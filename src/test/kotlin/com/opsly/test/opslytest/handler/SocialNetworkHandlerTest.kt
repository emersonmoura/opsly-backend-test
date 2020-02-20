package com.opsly.test.opslytest.handler

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.opsly.test.opslytest.model.FacebookEvent
import com.opsly.test.opslytest.model.InstagramEvent
import com.opsly.test.opslytest.model.TwitterEvent
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux

@SpringBootTest
class SocialNetworkHandlerTest : StringSpec() {

    override fun listeners() = listOf(SpringListener)

    @Autowired
    lateinit var socialNetworkHandler: SocialNetworkHandler

    init {

        "given a twitter body with items should compute each one" {
            configureMocks(Flux.just(), Flux.just(),Flux.just(TwitterEvent("name", "tweet")))

            val result = socialNetworkHandler.findEvents().block()

            result?.get("twitter")?.shouldHaveSize(1)
        }

        "given a twitter body with items should map its value" {
            configureMocks(Flux.just(), Flux.just(),Flux.just(TwitterEvent("name", "tweet")))

            val result = socialNetworkHandler.findEvents().block()

            result?.get("twitter")?.firstOrNull() shouldBe  "tweet"
        }

        "given a facebook body with items should compute each one" {
            configureMocks(Flux.just(FacebookEvent("name", "status")), Flux.just(), Flux.just())

            val result = socialNetworkHandler.findEvents().block()

            result?.get("facebook")?.shouldHaveSize(1)
        }

        "given a facebook body with items should map its value" {
            configureMocks(Flux.just(FacebookEvent("name", "status")), Flux.just(), Flux.just())

            val result = socialNetworkHandler.findEvents().block()

            result?.get("facebook")?.firstOrNull() shouldBe "status"
        }

        "given a instagram body with items should compute each one" {
            configureMocks(Flux.just(), Flux.just(InstagramEvent("name", "picture")), Flux.just())

            val result = socialNetworkHandler.findEvents().block()

            result?.get("instagram")?.shouldHaveSize(1)
        }

        "given a instagram body with items should map its value" {
            configureMocks(Flux.just(), Flux.just(InstagramEvent("name", "picture")), Flux.just())

            val result = socialNetworkHandler.findEvents().block()

            result?.get("instagram")?.firstOrNull() shouldBe "picture"
        }

        "empty results should not be considered" {
            configureMocks(Flux.just(FacebookEvent()), Flux.just(InstagramEvent()), Flux.just(TwitterEvent()))

            val result = socialNetworkHandler.findEvents().block()

            result?.get("facebook")?.shouldHaveSize(0)
        }
    }

    private fun configureMocks(facebookEvents: Flux<FacebookEvent>, instagramEvents: Flux<InstagramEvent>, twitterEvents: Flux<TwitterEvent>) {
        socialNetworkHandler.socialEventHandlers.clear()
        configureFacebookMock(facebookEvents)
        configureInstagramMock(instagramEvents)
        configureTwitterMock(twitterEvents)
    }

    private fun configureTwitterMock(twitterEvents: Flux<TwitterEvent>) {
        val twitterEventHandler = mock<TwitterEventHandler> {
            on { findEvents<TwitterEvent>() } doReturn twitterEvents
        }
        socialNetworkHandler.socialEventHandlers.add(twitterEventHandler)
    }

    private fun configureInstagramMock(instagramEvents: Flux<InstagramEvent>) {
        val instagramEventHandler = mock<InstagramEventHandler> {
            on { findEvents<InstagramEvent>() } doReturn instagramEvents
        }
        socialNetworkHandler.socialEventHandlers.add(instagramEventHandler)
    }

    private fun configureFacebookMock(facebookEvents: Flux<FacebookEvent>) {
        val facebookEventHandler = mock<FacebookEventHandler> {
           on { findEvents<FacebookEvent>() } doReturn facebookEvents
        }
        socialNetworkHandler.socialEventHandlers.add(facebookEventHandler)
    }

}