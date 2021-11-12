package com.satya.goesjogja.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.satya.goesjogja.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    //private var doubleBackPressedOnce = false

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {

        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    /*fun doubleBackToExit(){

        if(doubleBackPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackPressedOnce = true

        Toast.makeText(this, resources.getString(R.string.click_back_again), Toast.LENGTH_SHORT).show()

        Handler().postDelayed( {doubleBackPressedOnce = false}, 2000)
    } */
}