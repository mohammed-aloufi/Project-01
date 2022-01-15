package com.example.scrolly.feature_timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.models.Post
import com.example.scrolly.models.User
import com.example.scrolly.repositories.FirebaseRepo
import kotlinx.coroutines.launch

private const val TAG = "TimelineViewModel"

class TimelineViewModel : ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()

    fun isUserLoggedIn(): Boolean {
        return firebaseRepo.isUserLoggedIn()
    }

    fun getPosts(): LiveData<List<Post>>{
        return firebaseRepo.getPosts()
    }

    fun currentUserId(): String? = firebaseRepo.firebaseAuth.currentUser?.uid

    suspend fun getUserInfo(id: String): User {
        return firebaseRepo.getUserInfo(id)!!
    }

    fun addLike(postId: String){
        firebaseRepo.addLike(postId)
    }

    fun deleteLike(postId: String){
        viewModelScope.launch {
            firebaseRepo.deleteLike(postId)
        }
    }

}