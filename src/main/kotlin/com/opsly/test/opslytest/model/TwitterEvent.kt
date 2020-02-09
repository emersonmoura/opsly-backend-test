package com.opsly.test.opslytest.model

data class TwitterEvent(val username: String? = null, val tweet: String? = null){
    fun hasTweet(): Boolean {
        return tweet != null
    }
}