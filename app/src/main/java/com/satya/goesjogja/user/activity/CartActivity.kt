package com.satya.goesjogja.user.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityCartBinding
import com.satya.goesjogja.user.adapter.CartAdapter
import com.satya.goesjogja.user.model.Cart

class CartActivity : BaseActivity() {

    private lateinit var cartBinding: ActivityCartBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mWisataList: ArrayList<Wisata>

    companion object {
        const val USER_ID: String = "user_id"
        const val CART_ITEMS: String = "cart_items"
        const val JUMLAH_TIKET: String = "jumlahTiket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartBinding = ActivityCartBinding.inflate(layoutInflater)
        val view = cartBinding.root
        setContentView(view)

        cartBinding.btnChekout.setOnClickListener {
            val intent = Intent(this@CartActivity, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }


    fun getCartList(activity: Activity) {
        mFireStore.collection(CART_ITEMS)
                .whereEqualTo(USER_ID, LoginActivity().getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(javaClass.simpleName, document.documents.toString())
                    val list: ArrayList<Cart> = ArrayList()

                    for (i in document.documents) {
                        val cartItem = i.toObject(Cart::class.java)!!
                        cartItem.id = i.id
                        list.add(cartItem)
                    }

                    when(activity){
                        is CartActivity -> {
                            checkCartList(list)
                        }
                        is CheckoutActivity -> {
                            activity.checkCartList(list)
                        }

                    }

                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while getting list cart")
                }
    }

    private fun checkCartList(cartList: ArrayList<Cart>){

        hideProgressDialog()

        if(cartList.size > 0){
            cartBinding.rvCartItem.visibility = View.VISIBLE
            cartBinding.tvKosong.visibility = View.GONE

            cartBinding.rvCartItem.layoutManager = LinearLayoutManager(this)
            cartBinding.rvCartItem.setHasFixedSize(true)
            val cartAdapter = CartAdapter(this, cartList,true)
            cartBinding.rvCartItem.adapter = cartAdapter

            var subTotal: Double = 0.0

            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.jumlahTiket.toInt()
                subTotal += (price * quantity)
            }

            cartBinding.tvTotal.text = "Rp${subTotal}"

            if (subTotal > 0) {
                cartBinding.btnChekout.visibility = View.VISIBLE
            }
        }

        else{
            cartBinding.rvCartItem.visibility = View.GONE
            cartBinding.tvKosong.visibility = View.VISIBLE
        }
    }

    fun getAllWisataList(activity: Activity) {

        mFireStore.collection(WisataActivity.WISATA)
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Wisata List", document.documents.toString())
                    val wisataList: ArrayList<Wisata> = ArrayList()
                    for (i in document.documents) {
                        val wisata = i.toObject(Wisata::class.java)
                        wisata!!.wisata_id = i.id
                    }

                    when(activity){
                        is CartActivity -> {
                            mWisataList = wisataList
                            getCartList(this)
                        }
                        is CheckoutActivity -> {
                            mWisataList = wisataList
                            activity.getCartList()
                        }
                    }

                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while getting list wisata")
                }
    }

    private fun successGetAllWisataList() {
        showProgressDialog(resources.getString(R.string.loading))
        getAllWisataList(this)
    }



    fun updateCart(cartId: String, hashMap: HashMap<String, Any>) {
        mFireStore.collection(CART_ITEMS)
                .document(cartId)
                .update(hashMap)
                .addOnSuccessListener {
                    hideProgressDialog()
                    getCartList(this)
                }
                .addOnFailureListener { e ->
                    Log.e(javaClass.simpleName, "Error while updating the cart")
                }
    }

    fun removeItemFromCart(chartId: String) {
        mFireStore.collection(CART_ITEMS)
                .document(chartId)
                .delete()
                .addOnSuccessListener {
                    hideProgressDialog()
                    getCartList(this)
                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while remove the cart")
                }
    }

    override fun onResume() {
        super.onResume()
        successGetAllWisataList()
    }
}