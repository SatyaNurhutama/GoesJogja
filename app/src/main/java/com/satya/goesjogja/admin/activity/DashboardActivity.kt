package com.satya.goesjogja.admin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityDashboardBinding
import com.satya.goesjogja.user.activity.LoginActivity
import com.satya.goesjogja.user.activity.RegisterActivity
import com.satya.goesjogja.user.activity.UserProfileActivity
import com.satya.goesjogja.user.model.User

class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dashboardBinding: ActivityDashboardBinding
    private lateinit var mUser: User
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = dashboardBinding.root
        setContentView(view)
        getDataUser()
        dashboardBinding.tvWisata.setOnClickListener(this)
        dashboardBinding.tvOrder.setOnClickListener(this)
        dashboardBinding.ivProfile.setOnClickListener(this)
        dashboardBinding.llProfile.setOnClickListener(this)
        supportActionBar?.hide()
    }

    private fun getDataUser(){
        mFireStore.collection(RegisterAdminActivity.USER)
            .document(LoginAdminActivity().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = this?.getSharedPreferences(
                    LoginActivity.GOESJOGJA_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                editor.putString(
                    LoginActivity.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                mUser = user

                Glide.with(this)
                    .load(user.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(dashboardBinding.ivProfile)

                dashboardBinding.tvName.text = "${user.firstName} ${user.lastName}"

            }
            .addOnFailureListener { e ->
                Log.e(
                    javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tvWisata -> {
                    val intent = Intent(this, ListWisataAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.tvOrder -> {
                    val intent = Intent(this, OrderAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.iv_profile -> {
                    val intent = Intent(this, ProfileAdminActivity::class.java)
                    startActivity(intent)
                }

                R.id.ll_profile -> {
                    val intent = Intent(this, ProfileAdminActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}