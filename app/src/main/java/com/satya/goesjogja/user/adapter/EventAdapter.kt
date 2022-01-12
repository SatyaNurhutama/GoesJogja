package com.satya.goesjogja.user.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.databinding.ItemEventBinding
import com.satya.goesjogja.databinding.ItemListBinding
import com.satya.goesjogja.user.activity.DetailEventActivity
import com.satya.goesjogja.user.activity.DetailEventActivity.Companion.EXTRA_DESKRIPSI
import com.satya.goesjogja.user.activity.DetailEventActivity.Companion.EXTRA_LOKASI
import com.satya.goesjogja.user.activity.DetailEventActivity.Companion.EXTRA_NAME
import com.satya.goesjogja.user.activity.DetailEventActivity.Companion.EXTRA_PHOTO
import com.satya.goesjogja.user.activity.DetailEventActivity.Companion.EXTRA_TANGGAL
import com.satya.goesjogja.user.data.EventData
import com.satya.goesjogja.user.model.Event

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private val list = ArrayList<Event>()

    fun setList(event: List<Event>?){
        if (event == null) return
        this.list.clear()
        this.list.addAll(EventData.listEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = list[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = list.size

    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event){
            with(binding){
                tvNamaEvent.text = event.name
                tvDeskripsiEvent.text = event.deskripsi

                Glide.with(itemView.context)
                    .load(event.images)
                    .into(imgEvent)

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailEventActivity::class.java)
                    intent.putExtra(EXTRA_NAME, event.name)
                    intent.putExtra(EXTRA_TANGGAL, event.tanggal)
                    intent.putExtra(EXTRA_DESKRIPSI, event.deskripsi)
                    intent.putExtra(EXTRA_LOKASI, event.lokasi)
                    intent.putExtra(EXTRA_PHOTO, event.images)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}