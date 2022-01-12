package com.satya.goesjogja.user.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    var name: String = "",
    var tanggal: String = "",
    var deskripsi: String = "",
    var lokasi: String = "",
    var images: Int = 0
) : Parcelable
