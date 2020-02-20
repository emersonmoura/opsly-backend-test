package com.opsly.test.opslytest.model

data class TwitterEvent(val username: String? = null, val tweet: String? = null):SocialEvent{

    override fun information(): String? {
        return this.tweet
    }

    override fun networkName(): String {
        return "twitter"
    }
}