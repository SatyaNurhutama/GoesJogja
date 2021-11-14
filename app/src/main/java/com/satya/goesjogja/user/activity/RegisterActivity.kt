package com.satya.goesjogja.user.activity

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityRegisterBinding
import com.satya.goesjogja.user.model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    companion object {
        const val USER: String = "user"
    }

    private lateinit var registerBinding: ActivityRegisterBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
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

        registerBinding.btnDaftar.setOnClickListener {
            registerUser()
        }

    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(registerBinding.etNamaDepan.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(registerBinding.etNamaBelakang.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(registerBinding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(registerBinding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(registerBinding.etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            registerBinding.etPassword.text.toString().trim { it <= ' ' } != registerBinding.etConfirmPassword.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            else -> {
                //showErrorSnackBar(resources.getString(R.string.data_valid), false)
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            showProgressDialog(resources.getString(R.string.loading))

            val email: String = registerBinding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = registerBinding.etPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                    firebaseUser.uid,
                                    registerBinding.etNamaDepan.text.toString().trim { it <= ' ' },
                                    registerBinding.etNamaBelakang.text.toString().trim { it <= ' ' },
                                    registerBinding.etEmail.text.toString().trim { it <= ' ' }
                            )

                            mFireStore.collection(USER)
                                    .document(user.id)
                                    .set(user, SetOptions.merge())
                                    .addOnSuccessListener {
                                        hideProgressDialog()
                                        Toast.makeText(this@RegisterActivity, resources.getString(R.string.daftar_sukses), Toast.LENGTH_SHORT).show()
                                        FirebaseAuth.getInstance().signOut()
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        hideProgressDialog()
                                        Log.e(javaClass.simpleName, "Error while registering the user")
                                    }
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

}