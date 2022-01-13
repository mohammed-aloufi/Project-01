package com.example.scrolly.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.findNavController
import com.example.scrolly.R
import com.example.scrolly.databinding.ActivitySplashBinding
import com.example.scrolly.feature_identity.login.LoginFragment
import com.example.scrolly.repositories.FirebaseRepo

const val SHARED_PREF_FILE="login state"
const val STATE="state"
const val USER_ID= "userId"

class Splash : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySplashBinding.inflate(layoutInflater)
        FirebaseRepo.init(this)
        setContentView(binding.root)
        val sharedPref= getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)


        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
              //
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
              //
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

//                if(sharedPref.getBoolean(STATE, false)) {
                    val intent = Intent(this@Splash,MainActivity::class.java)
                    startActivity(intent)
                    finish()
//                }else{
//                    val mFragmentManager =supportFragmentManager
//                    val loginFragment=LoginFragment()
//                    mFragmentManager.beginTransaction().add(R.id.frameLayout,loginFragment).commit()
////                    finish()
//                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
               //
            }
        })
}}