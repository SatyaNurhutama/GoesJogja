package com.satya.goesjogja.admin.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.adapter.OrderAdminAdapter
import com.satya.goesjogja.admin.model.SoldTicket
import com.satya.goesjogja.databinding.ActivityOrderAdminBinding
import com.satya.goesjogja.user.activity.CartActivity

class OrderAdminActivity : BaseActivity() {

    private lateinit var orderAdminBinding: ActivityOrderAdminBinding
    private val mFirestore =  FirebaseFirestore.getInstance()

    companion object{
        const val SOLD_TICKET: String = "sold_ticket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderAdminBinding = ActivityOrderAdminBinding.inflate(layoutInflater)
        val view = orderAdminBinding.root
        setContentView(view)

        supportActionBar?.hide()

        orderAdminBinding.btnBack.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        getSoldTicket()
    }

    private fun getSoldTicket(){
        showProgressDialog(resources.getString(R.string.loading))

        mFirestore.collection(SOLD_TICKET)
            .whereEqualTo(CartActivity.USER_ID, LoginAdminActivity().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<SoldTicket> = ArrayList()
                for (i in document.documents){
                    val soldTicket = i.toObject(SoldTicket::class.java)!!
                    soldTicket.id = i.id
                    list.add(soldTicket)
                }

                checkList(list)

            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e(javaClass.simpleName, "Error while getting the sold ticket")
            }
    }

    private fun checkList(soldList: ArrayList<SoldTicket>){
        hideProgressDialog()
        if(soldList.size > 0 ){
            orderAdminBinding.rvOrderItem.visibility = View.VISIBLE
            orderAdminBinding.tvKosong.visibility = View.GONE

            orderAdminBinding.rvOrderItem.layoutManager = LinearLayoutManager(this)
            orderAdminBinding.rvOrderItem.setHasFixedSize(true)

            val soldAdapter = OrderAdminAdapter(this)
            soldAdapter.setList(soldList)
            orderAdminBinding.rvOrderItem.adapter = soldAdapter
        } else {
            orderAdminBinding.rvOrderItem.visibility = View.GONE
            orderAdminBinding.tvKosong.visibility = View.VISIBLE
        }
    }
}