package com.opsly.test.opslytest.model

interface SocialEvent {

    fun information(): String?

    fun hasInformation(): Boolean = information() != null

    fun networkName() : String
}