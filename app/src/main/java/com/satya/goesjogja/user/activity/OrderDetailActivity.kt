package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.satya.goesjogja.databinding.ActivityOrderDetailBinding
import com.satya.goesjogja.user.adapter.CartAdapter
import com.satya.goesjogja.user.model.Order
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var orderDetailBinding: ActivityOrderDetailBinding

    companion object{
        const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailBinding = ActivityOrderDetailBinding.inflate(layoutInflater)
        val view = orderDetailBinding.root
        setContentView(view)

        supportActionBar!!.hide()

        var myOrderDetail: Order = Order()
        if(intent.hasExtra(EXTRA_MY_ORDER_DETAILS)){
            myOrderDetail = intent.getParcelableExtra<Order>(EXTRA_MY_ORDER_DETAILS)!!
        }

        setupData(myOrderDetail)
    }

    private fun setupData(orderDetails: Order) {

        orderDetailBinding.tvId.text = orderDetails.title

        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        orderDetailBinding.tvDate.text = orderDateTime

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        orderDetailBinding.rvCartItem.layoutManager = LinearLayoutManager(this@OrderDetailActivity)
        orderDetailBinding.rvCartItem.setHasFixedSize(true)

        val cartListAdapter = CartAdapter(this@OrderDetailActivity, orderDetails.items,false)
        orderDetailBinding.rvCartItem.adapter = cartListAdapter

        orderDetailBinding.tvTotal.text = "Rp ${orderDetails.total_amount}"
    }


}