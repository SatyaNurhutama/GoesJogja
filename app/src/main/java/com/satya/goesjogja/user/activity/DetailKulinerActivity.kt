package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.satya.goesjogja.databinding.ActivityDetailKulinerBinding

class DetailKulinerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKulinerBinding

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
        const val EXTRA_PHOTO = "extra_photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKulinerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.tvTitle.text = intent.getStringExtra(EXTRA_NAME)
        binding.tvDeskripsi.text = intent.getStringExtra(EXTRA_DESKRIPSI)

        Glide.with(this)
            .load(intent.getIntExtra(EXTRA_PHOTO, 0))
            .into(binding.imgKuliner)
    }
}