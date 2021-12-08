package com.satya.goesjogja.user.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.activity.DetailWisataActivity
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityCartBinding
import com.satya.goesjogja.user.adapter.CartAdapter
import com.satya.goesjogja.user.model.Cart

class CartActivity : BaseActivity() {

    private lateinit var cartBinding: ActivityCartBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mWisataList: ArrayList<Wisata>

    companion object{
        const val USER_ID: String = "user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartBinding = ActivityCartBinding.inflate(layoutInflater)
        val view = cartBinding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog(resources.getString(R.string.loading))
        getAllWisataList()
        hideProgressDialog()
    }

    private fun getCartList() {
        mFireStore.collection(DetailWisataActivity.CART_ITEMS)
                .whereEqualTo(USER_ID, LoginActivity().getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->
                    Log.e(javaClass.simpleName, document.documents.toString())
                    val list: ArrayList<Cart> = ArrayList()

                    for (i in document.documents) {
                        val cartItem = i.toObject(Cart::class.java)!!
                        cartItem.id = i.id

                        list.add(cartItem)

                        if (list.size > 0) {

                            cartBinding.rvCartItem.visibility = View.VISIBLE
                            cartBinding.tvKosong.visibility = View.GONE

                            cartBinding.rvCartItem.layoutManager = LinearLayoutManager(this)
                            cartBinding.rvCartItem.setHasFixedSize(true)
                            val cartAdapter = CartAdapter(this)
                            cartAdapter.setList(list)
                            cartBinding.rvCartItem.adapter = cartAdapter

                            var subTotal: Double = 0.0

                            for (item in list) {
                                val price = item.price.toDouble()
                                val quantity = item.jumlahTiket.toInt()
                                subTotal += (price * quantity)
                            }

                            if(subTotal > 0){
                                cartBinding.btnCheckout.visibility = View.VISIBLE
                            }

                        } else {
                            cartBinding.rvCartItem.visibility = View.GONE
                            cartBinding.tvKosong.visibility = View.VISIBLE
                        }

                    }
                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while getting list cart")
                }
    }

    private fun getAllWisataList(){
        mFireStore.collection(WisataActivity.WISATA)
                .get()
                .addOnSuccessListener { document ->
                    Log.e("Wisata List", document.documents.toString())
                    val wisataList: ArrayList<Wisata> = ArrayList()
                    for (i in document.documents){
                        val wisata = i.toObject(Wisata::class.java)
                        wisata!!.wisata_id = i.id
                    }

                    mWisataList = wisataList

                    getCartList()

                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while getting list wisata")
                }
    }


}