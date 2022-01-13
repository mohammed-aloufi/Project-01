package com.example.scrolly.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.example.scrolly.R
import com.example.scrolly.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"

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
//
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController=navHostFragment.navController
        setupActionBarWithNavController(navController)

        NavigationUI.setupWithNavController(binding.bottomNavView,navController)

        Log.d(TAG,"this is the destiny ${navController.currentDestination.toString()}")

//        val navController = binding.bottomNavView
//        navController.setupWithNavController(findNavController(R.id.fragmentContainerView))
        navController.addOnDestinationChangedListener{_,destenation,_ ->
            when(destenation.id){
                R.id.loginFragment -> binding.bottomNavView.visibility= View.GONE
                R.id.registerFragment -> binding.bottomNavView.visibility= View.GONE

                else -> binding.bottomNavView.visibility= View.VISIBLE
            }
        }





    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // this override function is to Double back press to exit from main fragment(timelineFragment.kt)
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
// added this conditon when using NavigationComponent
        if ( navController.currentDestination?.id==navController.graph.startDestination) {
            if(doubleBackToExitPressedOnce)
                super.onBackPressed()
            else{
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, getString(R.string.clickagain), Toast.LENGTH_SHORT).show()

                Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

            }


        }else{
            super.onBackPressed()
        }


    }


}