package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityListEventBinding
import com.satya.goesjogja.user.adapter.EventAdapter
import com.satya.goesjogja.user.model.Event

class ListEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListEventBinding
    val list : ArrayList<Event> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvEvent.layoutManager = LinearLayoutManager(this)
        binding.rvEvent.setHasFixedSize(true)

        val adapter = EventAdapter()
        adapter.setList(list)
        binding.rvEvent.adapter = adapter
    }
}