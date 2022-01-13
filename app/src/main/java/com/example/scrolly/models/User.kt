package com.example.scrolly.models

import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val userName: String = "",
    val profileImgUrl: String = ""
)
