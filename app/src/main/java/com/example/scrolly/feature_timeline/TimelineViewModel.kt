package com.example.scrolly.feature_timeline

import androidx.lifecycle.ViewModel
import com.example.scrolly.models.Post

class TimelineViewModel: ViewModel() {

    val dummyPosts = listOf(
        Post(postMessage = "Hello world"),
        Post(postMessage = "Hello world"),
        Post(postMessage = "Hello world"),
        Post(postMessage = "Hello world"),
        Post(postMessage = "Hello world"),
        Post(postMessage = "Hello world"),
    )
}