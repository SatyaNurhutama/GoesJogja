package com.satya.goesjogja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.satya.goesjogja.admin.activity.LoginAdminActivity
import com.satya.goesjogja.databinding.ActivityMainBinding
import com.satya.goesjogja.user.activity.LoginActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.btnUser.setOnClickListener(this)
        mainBinding.btnAdmin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_user -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_admin -> {
                    val intent = Intent(this, LoginAdminActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}