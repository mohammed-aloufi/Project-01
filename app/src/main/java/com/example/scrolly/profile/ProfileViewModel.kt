package com.example.scrolly.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.repositories.FirebaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()
    var imageUri: Uri? = null
    fun getUser(): FirebaseUser?{
        return FirebaseAuth.getInstance().currentUser
    }

    fun uploadProfileImage(userId: String, uri: Uri){
        viewModelScope.launch {
            firebaseRepo.uploadProfileImage(userId, uri)
        }
    }
}