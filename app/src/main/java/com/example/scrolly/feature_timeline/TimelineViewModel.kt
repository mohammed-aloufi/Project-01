package com.example.scrolly.feature_timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.models.Post
import com.example.scrolly.repositories.FirebaseRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TimelineViewModel: ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()

    fun getPosts(): LiveData<List<Post>>{
        return firebaseRepo.getPosts()
    }
}