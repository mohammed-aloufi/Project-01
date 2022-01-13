package com.example.scrolly.feature_addPost

import android.net.Uri
import androidx.lifecycle.*
import com.example.scrolly.models.Post
import com.example.scrolly.repositories.FirebaseRepo

class AddPostViewModel: ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()
    var imageUri: Uri? = null

    suspend fun uploadPost(post: Post, postImgUri: Uri?): Boolean{
        return firebaseRepo.uploadPost(post, postImgUri)
    }
}