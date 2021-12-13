package com.satya.goesjogja.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldTicket(
    val user_id: String = "",
    val title: String = "",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",
    val order_id: String = "",
    val order_date: Long = 0L,
    val total_amount: String = "",
    var id: String = "",
):Parcelable
