package com.example.scrolly.feature_identity.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.repositories.FirebaseRepo
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "RegisterViewModel"

class RegisterViewModel:ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()

    val signUpLiveData = MutableLiveData<String>()
    val signUpErrorLiveData = MutableLiveData<String>()


    fun signUp(email:String, password: String, fullname: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.register(email, password)

                response.addOnCompleteListener {
                    if (it.isSuccessful) {
                        insertUserInfo(fullname)
                    }else{
                        signUpErrorLiveData.postValue(response.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: ${e.message}")
                signUpErrorLiveData.postValue(e.message)
            }
        }
    }
    private fun insertUserInfo(fullname:String) {


        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(fullname)
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebaseRepo.firebaseAuth.currentUser?.updateProfile(profileUpdates)

                signUpLiveData.postValue("Success")

            } catch(e: Exception) {
                signUpErrorLiveData.postValue(e.message)

            }
        }

    }

}