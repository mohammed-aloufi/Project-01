package com.example.scrolly.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.example.scrolly.R
import com.example.scrolly.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        val navController = binding.bottomNavView
        navController.setupWithNavController(findNavController(R.id.fragmentContainerView))

    }
}