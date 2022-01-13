package com.example.scrolly.utils

import android.view.View
import com.example.scrolly.R
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, message: String, isSuccessful: Boolean) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    if (isSuccessful){
        snackBar.setBackgroundTint(view.resources.getColor(R.color.light_green))
    }else{
        snackBar.setBackgroundTint(view.resources.getColor(R.color.light_red))
        snackBar.setTextColor(view.resources.getColor(R.color.white))
    }
    snackBar.show()
}