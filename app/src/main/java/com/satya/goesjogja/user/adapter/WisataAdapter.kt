package com.satya.goesjogja.user.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.admin.activity.DetailAdminActivity
import com.satya.goesjogja.admin.activity.DetailWisataActivity
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ItemWisataBinding

class WisataAdapter() : RecyclerView.Adapter<WisataAdapter.WisataViewHolder>() {

    private val list = ArrayList<Wisata>()

    fun setList(wisata: List<Wisata>?){
        if(wisata == null) return
        this.list.clear()
        this.list.addAll(wisata)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WisataAdapter.WisataViewHolder {
        val itemWisataBinding = ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataViewHolder(itemWisataBinding)
    }

    override fun onBindViewHolder(holder: WisataAdapter.WisataViewHolder, position: Int) {
        val wisata = list[position]
        holder.bind(wisata)
    }

    override fun getItemCount(): Int = list.size

    inner class WisataViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: Wisata){
            with(binding){
                tvNamaWisata.text = wisata.nama
                tvLokasi.text = wisata.lokasi
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailWisataActivity::class.java)
                    intent.putExtra(DetailWisataActivity.EXTRA_WISATA_ID, wisata.wisata_id)
                    intent.putExtra(DetailWisataActivity.EXTRA_WISATA_OWNER_ID, wisata.admin_id)
                    itemView.context.startActivity(intent)
                }

                Glide.with(itemView.context)
                    .load(wisata.image)
                    .into(imgWisata)
            }
        }
    }
}