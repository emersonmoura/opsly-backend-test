package com.opsly.test.opslytest.model

data class InstagramEvent(val username: String? = null, val picture: String? = null):SocialEvent{

    override fun information(): String? {
        return picture
    }

    override fun networkName(): String {
        return "instagram"
    }
}