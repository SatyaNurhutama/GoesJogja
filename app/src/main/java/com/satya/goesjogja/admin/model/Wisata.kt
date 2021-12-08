package com.satya.goesjogja.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wisata(
        val admin_id: String = "",
        val username: String = "",
        val nama: String = "",
        val deskripsi: String = "",
        val harga: String = "",
        val lokasi: String = "",
        val image: String = "",
        var wisata_id: String = ""
) : Parcelable