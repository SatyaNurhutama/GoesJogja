 package com.satya.goesjogja.admin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.admin.activity.DetailAdminActivity
import com.satya.goesjogja.admin.activity.DetailWisataActivity
import com.satya.goesjogja.admin.activity.ListWisataAdminActivity
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ItemListBinding

class WisataAdminAdapter(private val activity: ListWisataAdminActivity) : RecyclerView.Adapter<WisataAdminAdapter.WisataAdminViewHolder>() {

    private var listWisata = ArrayList<Wisata>()

    fun setList(wisata: List<Wisata>?){
        if(wisata == null) return
        this.listWisata.clear()
        this.listWisata.addAll(wisata)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WisataAdminViewHolder {
        val itemListBinding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WisataAdminViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: WisataAdminViewHolder, position: Int) {
        val wisata = listWisata[position]
        holder.bind(wisata)
    }

    override fun getItemCount(): Int = listWisata.size

    inner class WisataAdminViewHolder(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(wisata: Wisata){
            with(binding){
                tvNamaWisata.text = wisata.nama
                tvTotal.text = "Rp. ${wisata.harga}"
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailWisataActivity::class.java)
                    intent.putExtra(DetailAdminActivity.EXTRA_WISATA_ID, wisata.wisata_id)
                    intent.putExtra(DetailAdminActivity.EXTRA_WISATA_ADMIN_ID, wisata.admin_id)
                    itemView.context.startActivity(intent)
                }

                tvHapus.setOnClickListener {
                    activity.alertDeleteWisata(wisata.wisata_id)
                }

                Glide.with(itemView.context)
                        .load(wisata.image)
                        .into(imgWisata)

            }
        }
    }
}