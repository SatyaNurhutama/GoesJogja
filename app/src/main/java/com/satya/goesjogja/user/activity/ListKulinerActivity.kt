package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.satya.goesjogja.databinding.ActivityListKulinerBinding
import com.satya.goesjogja.user.adapter.KulinerAdapter
import com.satya.goesjogja.user.model.Kuliner

class ListKulinerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListKulinerBinding
    val list : ArrayList<Kuliner> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKulinerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.rvKuliner.layoutManager = LinearLayoutManager(this)
        binding.rvKuliner.setHasFixedSize(true)

        val adapter = KulinerAdapter()
        adapter.setList(list)
        binding.rvKuliner.adapter = adapter
    }
}