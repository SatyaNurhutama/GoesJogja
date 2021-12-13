package com.satya.goesjogja.user.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseFragment
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.FragmentOrderBinding
import com.satya.goesjogja.user.activity.CartActivity
import com.satya.goesjogja.user.activity.CheckoutActivity
import com.satya.goesjogja.user.activity.LoginActivity
import com.satya.goesjogja.user.adapter.OrderAdapter
import com.satya.goesjogja.user.model.Order

class OrderFragment : BaseFragment() {

    private lateinit var orderBinding: FragmentOrderBinding
    private var mFireStore = FirebaseFirestore.getInstance()

    companion object{
        const val USER_ID: String = "user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        orderBinding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        return orderBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cart -> {
                startActivity(Intent(activity, CartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getOrderList(){
        showProgressDialog(resources.getString(R.string.loading))

        mFireStore.collection(CheckoutActivity.ORDERS)
            .whereEqualTo(USER_ID, LoginActivity().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()
                for (i in document.documents){
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id
                    list.add(orderItem)
                }

                checkOrder(list)

            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e(
                    javaClass.simpleName,
                    "Error while getting order list items list",
                    e
                )
            }
    }

    private fun checkOrder(orderList: ArrayList<Order>){

        hideProgressDialog()

        if(orderList.size > 0){
            orderBinding.rvOrderItem.visibility = View.VISIBLE
            orderBinding.tvKosong.visibility = View.GONE

            orderBinding.rvOrderItem.layoutManager = LinearLayoutManager(activity)
            orderBinding.rvOrderItem.setHasFixedSize(true)

            val orderAdapter = OrderAdapter(requireActivity())
            orderAdapter.setList(orderList)
            orderBinding.rvOrderItem.adapter = orderAdapter
        } else{
            orderBinding.rvOrderItem.visibility = View.GONE
            orderBinding.tvKosong.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getOrderList()
    }



}