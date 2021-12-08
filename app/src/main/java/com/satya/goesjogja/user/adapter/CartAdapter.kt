package com.satya.goesjogja.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.databinding.CartItemsBinding
import com.satya.goesjogja.databinding.ItemWisataBinding
import com.satya.goesjogja.user.activity.CartActivity
import com.satya.goesjogja.user.model.Cart

class CartAdapter(activity: CartActivity) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val list = ArrayList<Cart>()

    fun setList(cart: List<Cart>?){
        if(cart == null) return
        this.list.clear()
        this.list.addAll(cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemCartBinding = CartItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(itemCartBinding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = list[position]
        holder.bind(cart)
    }

    override fun getItemCount(): Int = list.size

    inner class CartViewHolder(private val binding: CartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart){
            with(binding){
                tvNamaWisata.text = cart.title
                tvHarga.text = cart.price
                tvCartQuantity.text = cart.jumlahTiket

                Glide.with(itemView.context)
                        .load(cart.image)
                        .into(imgWisata)
            }
            }
        }

    }