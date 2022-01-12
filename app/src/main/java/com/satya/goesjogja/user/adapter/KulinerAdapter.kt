package com.satya.goesjogja.user.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.databinding.ItemEventBinding
import com.satya.goesjogja.user.activity.DetailEventActivity
import com.satya.goesjogja.user.activity.DetailKulinerActivity
import com.satya.goesjogja.user.activity.DetailKulinerActivity.Companion.EXTRA_DESKRIPSI
import com.satya.goesjogja.user.activity.DetailKulinerActivity.Companion.EXTRA_NAME
import com.satya.goesjogja.user.activity.DetailKulinerActivity.Companion.EXTRA_PHOTO
import com.satya.goesjogja.user.data.EventData
import com.satya.goesjogja.user.data.KulinerData
import com.satya.goesjogja.user.model.Event
import com.satya.goesjogja.user.model.Kuliner

class KulinerAdapter : RecyclerView.Adapter<KulinerAdapter.KulinerViewHolder>() {

    private val list = ArrayList<Kuliner>()

    fun setList(kuliner: List<Kuliner>?){
        if (kuliner == null) return
        this.list.clear()
        this.list.addAll(KulinerData.listKuliner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KulinerViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KulinerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KulinerViewHolder, position: Int) {
        val kuliner = list[position]
        holder.bind(kuliner)
    }

    override fun getItemCount(): Int = list.size

    class KulinerViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kuliner: Kuliner){
            with(binding){
                tvNamaEvent.text = kuliner.name
                tvDeskripsiEvent.text = kuliner.deskripsi

                Glide.with(itemView.context)
                    .load(kuliner.images)
                    .into(imgEvent)

                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailKulinerActivity::class.java)
                    intent.putExtra(EXTRA_NAME, kuliner.name)
                    intent.putExtra(EXTRA_DESKRIPSI, kuliner.deskripsi)
                    intent.putExtra(EXTRA_PHOTO, kuliner.images)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}