package com.opsly.test.opslytest.model

data class FacebookEvent(val name: String? = null, val status: String? = null) : SocialEvent{

    override fun information(): String? {
        return status
    }

    override fun networkName(): String {
        return "facebook"
    }
}