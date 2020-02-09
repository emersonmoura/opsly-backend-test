package com.opsly.test.opslytest.model

data class InstagramEvent(val username: String? = null, val picture: String? = null){
    fun hasPicture(): Boolean {
        return picture != null
    }
}