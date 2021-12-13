package com.satya.goesjogja.user.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.CartItemsBinding
import com.satya.goesjogja.user.activity.CartActivity
import com.satya.goesjogja.user.model.Cart

class CartAdapter(private val context: Context, private var list: ArrayList<Cart>, private val updateCartItems: Boolean) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun setList(cart: List<Cart>?) {
        if (cart == null) return
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
        fun bind(cart: Cart) {
            with(binding) {
                tvNamaWisata.text = cart.title
                tvHarga.text = cart.price
                tvCartQuantity.text = cart.jumlahTiket

                Glide.with(itemView.context)
                        .load(cart.image)
                        .into(imgWisata)

                if(updateCartItems){
                    ibRemoveCartItem.visibility = View.VISIBLE
                    ibAddCartItem.visibility = View.VISIBLE
                    tvHapus.visibility = View.VISIBLE
                } else {
                    ibRemoveCartItem.visibility = View.GONE
                    ibAddCartItem.visibility = View.GONE
                    tvHapus.visibility = View.GONE
                }

                tvHapus.setOnClickListener {
                    when(context){
                        is CartActivity -> {
                            context.showProgressDialog(context.resources.getString(R.string.loading))
                            context.removeItemFromCart(cart.id)
                        }
                    }
                }

                ibRemoveCartItem.setOnClickListener {
                    if (cart.jumlahTiket == "1") {
                        if(context is CartActivity){
                            context.showProgressDialog(context.resources.getString(R.string.loading))
                            context.removeItemFromCart(cart.id)
                        }
                    } else {
                        val cartQuantity: Int = cart.jumlahTiket.toInt()
                        val itemHashMap = HashMap<String, Any>()

                        itemHashMap[CartActivity.JUMLAH_TIKET] = (cartQuantity - 1).toString()

                        if(context is CartActivity){
                            context.showProgressDialog(context.resources.getString(R.string.loading))

                            context.updateCart(cart.id, itemHashMap)
                        }
                    }
                }

                ibAddCartItem.setOnClickListener {
                    val cartQuantity: Int = cart.jumlahTiket.toInt()
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[CartActivity.JUMLAH_TIKET] = (cartQuantity + 1).toString()

                    if(context is CartActivity){
                        context.showProgressDialog(context.resources.getString(R.string.loading))
                        context.updateCart(cart.id, itemHashMap)
                    }
                }
            }
        }

    }
}