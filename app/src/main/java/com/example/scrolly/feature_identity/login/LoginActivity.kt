package com.example.scrolly.feature_identity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scrolly.R
import com.example.scrolly.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}