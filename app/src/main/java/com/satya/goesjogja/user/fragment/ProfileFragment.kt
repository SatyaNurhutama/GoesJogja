package com.satya.goesjogja.user.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseFragment
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.FragmentProfileBinding
import com.satya.goesjogja.user.activity.LoginActivity
import com.satya.goesjogja.user.activity.RegisterActivity
import com.satya.goesjogja.user.activity.UserProfileActivity
import com.satya.goesjogja.user.model.User

class ProfileFragment : BaseFragment(), View.OnClickListener {

    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var mUser: User
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        profileBinding.btnLogOut.setOnClickListener(this)
        profileBinding.tvEdit.setOnClickListener(this)

        return profileBinding.root

    }
    
    fun getDataUser(){
        showProgressDialog(resources.getString(R.string.loading))
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

                    hideProgressDialog()
                    Glide.with(this)
                            .load(user.image)
                            .centerCrop()
                            .placeholder(R.drawable.ic_user_placeholder)
                            .into(profileBinding.imgProfile)

                    profileBinding.tvName.text = "${user.firstName} ${user.lastName}"
                    profileBinding.tvEmail.text = user.email

                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(
                            javaClass.simpleName,
                            "Error while getting user details.",
                            e
                    )
                }
    }

    override fun onResume() {
        super.onResume()
        getDataUser()
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()
                }
                R.id.tv_edit -> {
                    val intent = Intent(activity, UserProfileActivity::class.java)
                    intent.putExtra(LoginActivity.EXTRA_USER_DETAILS, mUser)
                    startActivity(intent)
                }
            }
        }
    }


}