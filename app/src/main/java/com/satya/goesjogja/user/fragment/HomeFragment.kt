package com.satya.goesjogja.user.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.FragmentHomeBinding
import com.satya.goesjogja.user.activity.*
import com.satya.goesjogja.user.model.User

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var mUser: User
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        homeBinding.tvWisata.setOnClickListener(this)
        homeBinding.tvEvent.setOnClickListener(this)
        homeBinding.tvKuliner.setOnClickListener(this)
        homeBinding.ivCart.setOnClickListener(this)
        getDataUser()
        return homeBinding.root
    }

    private fun getDataUser(){
        mFireStore.collection(RegisterActivity.USER)
            .document(LoginActivity().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity?.getSharedPreferences(
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
                    .into(homeBinding.ivProfile)

                homeBinding.tvName.text = "${user.firstName} ${user.lastName}"

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
                R.id.tv_wisata ->
                    startActivity(Intent(activity, WisataActivity::class.java))

                R.id.tv_event ->
                    startActivity(Intent(activity, ListEventActivity::class.java))

                R.id.tv_kuliner ->
                    startActivity(Intent(activity, ListKulinerActivity::class.java))

                R.id.iv_cart ->
                    startActivity(Intent(activity, CartActivity::class.java))
            }
        }
    }
}