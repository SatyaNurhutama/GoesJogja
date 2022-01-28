package com.satya.goesjogja.admin.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.admin.activity.DetailOrderAdminActivity
import com.satya.goesjogja.admin.model.SoldTicket
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ItemListBinding
import com.satya.goesjogja.databinding.ItemPesananBinding

class OrderAdminAdapter(private val activity: Activity) : RecyclerView.Adapter<OrderAdminAdapter.OrderAdminViewHolder>() {

    private var list = ArrayList<SoldTicket>()

    fun setList(soldTicket: List<SoldTicket>?){
        if(soldTicket == null) return
        this.list.clear()
        this.list.addAll(soldTicket)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdminAdapter.OrderAdminViewHolder {
        val binding = ItemPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAdminAdapter.OrderAdminViewHolder, position: Int) {
        val soldTicket = list[position]
        holder.bind(soldTicket)
    }

    override fun getItemCount(): Int = list.size

    inner class OrderAdminViewHolder(private val binding: ItemPesananBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(soldTicket: SoldTicket){
            with(binding){
                tvNamaWisata.text = soldTicket.title
                tvId.text = soldTicket.order_id
                tvTotal.text = "Rp ${soldTicket.total_amount}"

                Glide.with(itemView.context)
                    .load(soldTicket.image)
                    .into(imgWisata)

                itemView.setOnClickListener{
                    val intent = Intent(activity, DetailOrderAdminActivity::class.java)
                    intent.putExtra(DetailOrderAdminActivity.EXTRA_SOLD_PRODUCT_DETAILS, soldTicket)
                    activity.startActivity(intent)
                }
            }
        }
    }

}