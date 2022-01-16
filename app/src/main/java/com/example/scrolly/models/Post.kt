package com.example.scrolly.models

import java.util.*

data class Post(
    val id: String = UUID.randomUUID().toString(),
    var postMessage: String? = null,
    val postImageUrl: String? = null,
    var likes: MutableList<Like> = mutableListOf(),
    val timestamp: Long? = Date().time,
    var userId: String = ""
)