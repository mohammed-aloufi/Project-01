package com.example.scrolly.models

import java.util.*

data class Post(
    val id: String = UUID.randomUUID().toString(),
    var postMessage: String? = null,
    val postImageUrl: String? = null,
    val likes: Int = 0,
    val timestamp: Long? = Date().time,
    val userId: String = ""
)