package com.example.scrolly.feature_identity.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.models.User
import com.example.scrolly.repositories.FirebaseRepo
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "RegisterViewModel"

class RegisterViewModel:ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()

    val registerLiveData = MutableLiveData<String>()
    val registerErrorLiveData = MutableLiveData<String>()


    fun signUp(email:String, password: String, display: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.register(email, password)

                response.addOnCompleteListener {
                    if (it.isSuccessful) {
                        adddDisplayName(display)
                        firebaseRepo.createProfile(User(userName = display))
                    }else{
                        registerErrorLiveData.postValue(response.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                registerErrorLiveData.postValue(e.message)
            }
        }
    }


    ////////////
    private fun adddDisplayName(displayName:String) {


        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebaseRepo.firebaseAuth.currentUser?.updateProfile(profileUpdates)

                registerLiveData.postValue("Success")

            } catch(e: Exception) {
                registerErrorLiveData.postValue(e.message)

            }
        }

    }

}