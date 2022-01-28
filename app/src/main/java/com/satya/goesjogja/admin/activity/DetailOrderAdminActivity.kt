package com.satya.goesjogja.admin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.adapter.OrderAdminAdapter
import com.satya.goesjogja.admin.model.SoldTicket
import com.satya.goesjogja.databinding.ActivityDetailOrderAdminBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrderAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderAdminBinding
    private val mFirestore =  FirebaseFirestore.getInstance()

    companion object{
        const val EXTRA_SOLD_PRODUCT_DETAILS: String = "extra_sold_product_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.hide()

        var wisataDetails: SoldTicket = SoldTicket()

        if(intent.hasExtra(EXTRA_SOLD_PRODUCT_DETAILS)){
            wisataDetails = intent.getParcelableExtra<SoldTicket>(EXTRA_SOLD_PRODUCT_DETAILS)!!
        }

        getDetailsOrder(wisataDetails)
    }

    private fun getDetailsOrder(soldTicket: SoldTicket){

        val soldList = ArrayList<SoldTicket>()

        binding.tvId.text = soldTicket.order_id

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = soldTicket.order_date
        binding.tvDate.text = formatter.format(calendar.time)

        binding.tvNamaWisata.text = soldTicket.title
        binding.tvHargaTiket.text = soldTicket.price

        Glide.with(this)
            .load(soldTicket.image)
            .into(binding.imgWisata)

//        val soldAdapter = OrderAdminAdapter(this)
//        soldAdapter.setList(soldList)
//        binding.rvCartItem.adapter = soldAdapter

        binding.tvCartQuantity.text = soldTicket.sold_quantity

        binding.tvTotal.text = "Rp ${soldTicket.total_amount}"


    }
}