package com.satya.goesjogja.user.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kuliner(
    var name: String = "",
    var deskripsi: String = "",
    var images: Int = 0
) : Parcelable
