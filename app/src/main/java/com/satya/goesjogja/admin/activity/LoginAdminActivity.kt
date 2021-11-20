package com.satya.goesjogja.admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityLoginAdminBinding

class LoginAdminActivity : AppCompatActivity() {

    private lateinit var loginAdminBinding: ActivityLoginAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginAdminBinding = ActivityLoginAdminBinding.inflate(layoutInflater)
        val view = loginAdminBinding.root
        setContentView(view)
    }
}