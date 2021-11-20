package com.satya.goesjogja.admin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Admin(
        val id: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val image: String = "",
        val mobile: Long = 0,
        val profileCompleted: Int = 0
) : Parcelable