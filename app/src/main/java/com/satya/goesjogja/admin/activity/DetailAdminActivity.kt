package com.satya.goesjogja.admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satya.goesjogja.R

class DetailAdminActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_WISATA_ID: String = "extra_wisata_id"
        const val EXTRA_WISATA_ADMIN_ID : String = "extra_wisata_admin_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_admin)
    }
}