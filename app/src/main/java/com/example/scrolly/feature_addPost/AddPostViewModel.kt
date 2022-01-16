package com.example.scrolly.feature_addPost

import android.net.Uri
import androidx.lifecycle.*
import com.example.scrolly.models.Post
import com.example.scrolly.models.User
import com.example.scrolly.repositories.FirebaseRepo
import com.google.firebase.auth.FirebaseUser

class AddPostViewModel: ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()
    var imageUri: Uri? = null

    fun isUserLoggedIn(): Boolean{
        return firebaseRepo.isUserLoggedIn()
    }

    suspend fun getUserInfo(id: String): User? {
        return firebaseRepo.getUserInfo(id)
    }

    suspend fun uploadPost(post: Post, postImgUri: Uri?): Boolean{
        return firebaseRepo.uploadPost(post, postImgUri)
    }
}