package com.example.scrolly.feature_identity.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrolly.repositories.FirebaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

class LoginViewMode:ViewModel() {

    private val firebaseRepo = FirebaseRepo.get()

    val loginLiveData = MutableLiveData<String>()
    val loginErrorLiveData = MutableLiveData<String>()


    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = firebaseRepo.signIn(email, password)

                response.addOnCompleteListener {
                    if (it.isSuccessful) {

                        // post success to live data
                        loginLiveData.postValue("success login")
                        Log.d(TAG, "Login success: $response")

                    } else {
                        Log.d(TAG, it.exception!!.message.toString())
                        loginErrorLiveData.postValue(it.exception!!.message)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Catch: login ${e.message}")
                loginErrorLiveData.postValue(e.message)
            }
        }
    }


}