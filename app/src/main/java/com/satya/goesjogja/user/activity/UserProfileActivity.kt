package com.satya.goesjogja.user.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.user.activity.LoginActivity.Companion.EXTRA_USER_DETAILS
import com.satya.goesjogja.user.activity.RegisterActivity.Companion.USER
import com.satya.goesjogja.databinding.ActivityUserProfileBinding
import com.satya.goesjogja.user.model.User
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val READ_STORAGE_PERMISSION_CODE = 2
        const val PICK_IMAGE_REQUEST_CODE = 2

        const val MALE: String = "male"
        const val FEMALE: String = "female"
        const val GENDER: String = "gender"
        const val MOBILE: String = "mobile"
        const val USER_PROFILE_IMAGE = "user_profile_image"
        const val IMAGE = "image"
        const val COMPLETE_USER_PROFILE: String = "profileCompleted"
        const val FIRST_NAME: String = "firstName"
        const val LAST_NAME: String = "lastName"
    }

    private lateinit var userProfileBinding: ActivityUserProfileBinding
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userProfileBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = userProfileBinding.root
        setContentView(view)

        supportActionBar!!.hide()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if (intent.hasExtra(EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(EXTRA_USER_DETAILS)!!
        }

        if(mUserDetails.profileCompleted == 0){
            userProfileBinding.etNamaDepan.isEnabled = false
            userProfileBinding.etNamaBelakang.isEnabled = false
            userProfileBinding.etNamaDepan.setText(mUserDetails.firstName)
            userProfileBinding.etNamaBelakang.setText(mUserDetails.lastName)
            userProfileBinding.etEmail.isEnabled = false
            userProfileBinding.etEmail.setText(mUserDetails.email)
        } else {

            userProfileBinding.etNamaDepan.setText(mUserDetails.firstName)
            userProfileBinding.etNamaBelakang.setText(mUserDetails.lastName)
            userProfileBinding.etEmail.setText(mUserDetails.email)

            Glide.with(this)
                    .load(mUserDetails.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(userProfileBinding.imgProfile)

            if(mUserDetails.mobile != 0L){
                userProfileBinding.etPhone.setText(mUserDetails.mobile.toString())
            }

            if(mUserDetails.gender == MALE){
                userProfileBinding.rbMale.isChecked = true
            } else {
                userProfileBinding.rbFemale.isChecked = true
            }

        }

        userProfileBinding.btnSave.setOnClickListener(this)
        userProfileBinding.imgProfile.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_save -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.loading))
                        if(mSelectedImageFileUri != null){
                            uploadImageToCloudStorage(mSelectedImageFileUri, USER_PROFILE_IMAGE)
                        } else{
                            updateUserProfileDetails()
                        }
                    }
                }
                R.id.img_profile -> {
                    if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        showImageChooser(this@UserProfileActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
            }
        }
    }

    private fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this@UserProfileActivity)
            } else {
                Toast.makeText(
                        this,
                        resources.getString(R.string.izin_penyimpanan_ditolak),
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!
                        Glide.with(this)
                                .load(Uri.parse(mSelectedImageFileUri!!.toString()))
                                .centerCrop()
                                .placeholder(R.drawable.ic_user_placeholder)
                                .into(userProfileBinding.imgProfile)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                                this@UserProfileActivity,
                                resources.getString(R.string.gagal_pilih_gambar),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(userProfileBinding.etPhone.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_pesan_nomor_telepon), false)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun updateUserProfileData(hashMap: HashMap<String, Any>) {
        mFireStore.collection(USER)
                .document(LoginActivity().getCurrentUserID())
                .update(hashMap)
                .addOnSuccessListener {
                    hideProgressDialog()
                    onBackPressed()
                    finish()
                }

                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "Error while updating the user details", e)
                }
    }

    private fun uploadImageToCloudStorage(imageFileUri: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                USER_PROFILE_IMAGE + System.currentTimeMillis() + "." + getFileExtension(imageFileUri)
        )
        sRef.putFile(imageFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.e(
                            "Firebase Image URL",
                            taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )

                    taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener { uri ->
                                Log.e("Downloadable Image URL", uri.toString())
                                hideProgressDialog()
                                mUserProfileImageURL = uri.toString()
                                updateUserProfileDetails()
                            }
                }

                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, e.message, e)

                }
    }

    fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun updateUserProfileDetails(){

        val userHashMap = HashMap<String, Any>()

        val firstName = userProfileBinding.etNamaDepan.text.toString().trim{ it <= ' '}
        val lastName = userProfileBinding.etNamaBelakang.text.toString().trim{ it <= ' '}

        if(firstName != mUserDetails.firstName){
            userHashMap[FIRST_NAME] = firstName
        }

        if(lastName != mUserDetails.lastName){
            userHashMap[LAST_NAME] = lastName
        }

        val mobileNumber = userProfileBinding.etPhone.text.toString().trim{ it <= ' ' }

        val gender = if(userProfileBinding.rbMale.isChecked){
            MALE
        }else {
            FEMALE
        }

        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[IMAGE] = mUserProfileImageURL
        }

        if(mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
            userHashMap[MOBILE] = mobileNumber.toLong()
        }

        if(gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[GENDER] = gender
        }

        userHashMap[GENDER] = gender

        userHashMap[COMPLETE_USER_PROFILE] = 1

        updateUserProfileData(userHashMap)

    }

}