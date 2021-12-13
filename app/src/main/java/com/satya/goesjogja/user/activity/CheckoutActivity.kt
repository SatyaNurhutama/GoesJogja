package com.satya.goesjogja.user.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityCheckoutBinding
import com.satya.goesjogja.user.adapter.CartAdapter
import com.satya.goesjogja.user.model.Cart
import com.satya.goesjogja.user.model.Order
import com.satya.goesjogja.admin.model.SoldTicket

class CheckoutActivity : BaseActivity(), View.OnClickListener {

    private lateinit var checkoutBinding: ActivityCheckoutBinding
    private lateinit var mWisataList: ArrayList<Wisata>
    private lateinit var mCartItemsList: ArrayList<Cart>
    private val mFireStore = FirebaseFirestore.getInstance()
    private var mTotalAmount: Double = 0.0
    private lateinit var mOrderDetails: Order

    companion object{
        const val ORDERS: String = "orders"
        const val SOLD_TICKET: String = "sold_ticket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutBinding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = checkoutBinding.root
        setContentView(view)

        getWisataList()

        checkoutBinding.btnOrder.setOnClickListener(this)
    }

    private fun getWisataList() {
        showProgressDialog(resources.getString(R.string.loading))
        CartActivity().getAllWisataList(this)
    }

    fun checkCartList(cartList: ArrayList<Cart>) {

        hideProgressDialog()

        if (cartList.size > 0) {

            mCartItemsList = cartList

            checkoutBinding.rvCartItem.layoutManager = LinearLayoutManager(this)
            checkoutBinding.rvCartItem.setHasFixedSize(true)
            val cartAdapter = CartAdapter(this, cartList, false)
            checkoutBinding.rvCartItem.adapter = cartAdapter

            var subTotal: Double = 0.0

            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.jumlahTiket.toInt()
                subTotal += (price * quantity)
            }

            checkoutBinding.tvTotal.text = "Rp${subTotal}"

            if (subTotal > 0) {
                mTotalAmount = subTotal
                checkoutBinding.btnOrder.visibility = View.VISIBLE
            }
        } else {
            checkoutBinding.rvCartItem.visibility = View.GONE
        }
    }

    fun getCartList() {
        CartActivity().getCartList(this)
    }

    private fun placeOrder() {
        mOrderDetails = Order(
            LoginActivity().getCurrentUserID(),
            mCartItemsList,
            "My Order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mTotalAmount.toString(),
            System.currentTimeMillis()
        )

        showProgressDialog(resources.getString(R.string.loading))
        placeOrderDatabase(mOrderDetails)
    }

    private fun placeOrderDatabase(order: Order){
        mFireStore.collection(ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                updateAllDetails(mCartItemsList, order)
            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e(
                    javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }

    private fun updateDetails(){
        hideProgressDialog()
        val intent = Intent(this@CheckoutActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun updateAllDetails(cartList: ArrayList<Cart>, order: Order){
        val writeBatch = mFireStore.batch()

        for(cartItem in cartList){

            val soldProduct = SoldTicket(
                cartItem.wisata_admin_id,
                cartItem.title,
                cartItem.price,
                cartItem.jumlahTiket,
                cartItem.image,
                order.title,
                order.order_datetime,
                order.total_amount,
            )

            val documentReference = mFireStore.collection(SOLD_TICKET).document(cartItem.wisata_id)

            writeBatch.set(documentReference, soldProduct)
        }

        for (cartItem in cartList){
            val documentReference = mFireStore.collection(CartActivity.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            updateDetails()
        }
    }

    override fun onClick(v: View?) {
        if(v!= null){
            when(v.id){
                R.id.btn_order -> {
                    placeOrder()
                }
            }
        }
    }
}