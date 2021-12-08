package com.satya.goesjogja.user.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    val user_id: String = "",
    val wisata_id: String = "",
    val title: String = "",
    val price : String = "",
    val image: String = "",
    val jumlahTiket: String = "",
    var id: String = ""
): Parcelable