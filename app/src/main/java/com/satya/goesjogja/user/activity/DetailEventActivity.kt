package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailEventBinding

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_TANGGAL = "extra_tanggal"
        const val EXTRA_LOKASI = "extra_lokasi"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
        const val EXTRA_PHOTO = "extra_photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvTitle.text = intent.getStringExtra(EXTRA_NAME)
        binding.tvTanggal.text = intent.getStringExtra(EXTRA_TANGGAL)
        binding.tvLokasi.text = intent.getStringExtra(EXTRA_LOKASI)
        binding.tvDeskripsi.text = intent.getStringExtra(EXTRA_DESKRIPSI)

        Glide.with(this)
            .load(intent.getIntExtra(EXTRA_PHOTO, 0))
            .into(binding.imgEvent)

    }
}