package com.satya.goesjogja.user.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.databinding.CartItemsBinding
import com.satya.goesjogja.databinding.ItemListBinding
import com.satya.goesjogja.user.activity.OrderDetailActivity
import com.satya.goesjogja.user.model.Cart
import com.satya.goesjogja.user.model.Order

class OrderAdapter(private val context: Context) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private val list = ArrayList<Order>()

    fun setList(order: List<Order>?) {
        if (order == null) return
        this.list.clear()
        this.list.addAll(order)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        val order = list[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = list.size

    inner class OrderViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order){
            with(binding){
                tvNamaWisata.text = order.title
                tvTotal.text = "Rp ${order.total_amount}"

                tvHapus.visibility = View.GONE

                Glide.with(itemView.context)
                    .load(order.image)
                    .into(imgWisata)

                itemView.setOnClickListener{
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.putExtra(OrderDetailActivity.EXTRA_MY_ORDER_DETAILS, order)
                    context.startActivity(intent)
                }
            }
        }

    }
}