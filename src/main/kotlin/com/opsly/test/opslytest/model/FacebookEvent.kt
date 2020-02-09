package com.opsly.test.opslytest.model

data class FacebookEvent(val name: String? = null, val status: String? = null){
    fun hasStatus(): Boolean {
        return status != null
    }
}