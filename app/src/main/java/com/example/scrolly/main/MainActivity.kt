package com.example.scrolly.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.example.scrolly.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private val localizationDelegate = LocalizationActivityDelegate(this)


    companion object {
        lateinit var instance:  MainActivity
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}