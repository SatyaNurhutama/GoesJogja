package com.satya.goesjogja.user.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityLoginBinding
import com.satya.goesjogja.user.model.User

class LoginActivity : BaseActivity(), View.OnClickListener {

    companion object{
        const val GOESJOGJA_PREFERENCES: String = "goesjogjaprefers"
        const val LOGGED_IN_USERNAME: String = "logged_in_username"
        const val EXTRA_USER_DETAILS: String = "extra_user_details"
    }

    private lateinit var loginBinding: ActivityLoginBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        loginBinding.btnLogin.setOnClickListener(this)
        loginBinding.tvDaftar.setOnClickListener(this)
        loginBinding.tvForgotPassword.setOnClickListener(this)
    }

    private fun validateLoginUser(): Boolean {
        return when {
            TextUtils.isEmpty(loginBinding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(loginBinding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun userLogin() {
        if (validateLoginUser()) {
            showProgressDialog(resources.getString(R.string.loading))

            val email: String = loginBinding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = loginBinding.etPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mFireStore.collection(RegisterActivity.USER)
                                    .document(getCurrentUserID())
                                    .get()
                                    .addOnSuccessListener { document ->
                                        Log.i(javaClass.simpleName, document.toString())
                                        val user = document.toObject(User::class.java)!!

                                        val sharedPreferences = getSharedPreferences(
                                                GOESJOGJA_PREFERENCES,
                                                Context.MODE_PRIVATE
                                        )

                                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                        editor.putString(
                                                LOGGED_IN_USERNAME,
                                                "${user.firstName} ${user.lastName}"
                                        )
                                        editor.apply()

                                        checkProfile(user)

                                    }
                                    .addOnFailureListener { e ->
                                        hideProgressDialog()
                                        Log.e(
                                                javaClass.simpleName,
                                                "Error while getting user details.",
                                                e
                                        )
                                    }
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
        }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    private fun checkProfile(user: User) {
        hideProgressDialog()

        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }
        finish()
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tv_daftar -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    userLogin()
                }
                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


}