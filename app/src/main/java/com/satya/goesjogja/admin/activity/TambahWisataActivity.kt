package com.satya.goesjogja.admin.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.activity.LoginAdminActivity.Companion.GOESJOGJA_PREFERENCES
import com.satya.goesjogja.admin.activity.LoginAdminActivity.Companion.LOGGED_IN_USERNAME
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityTambahWisataBinding
import com.satya.goesjogja.user.activity.UserProfileActivity
import java.io.IOException


class TambahWisataActivity : BaseActivity(), View.OnClickListener {

    private lateinit var tambahWisataBinding: ActivityTambahWisataBinding
    private var mSelectedImageFileUri: Uri? = null
    private var mWisataImageURL: String = ""
    private val mFireStore = FirebaseFirestore.getInstance()

    companion object{
        const val PICK_IMAGE_REQUEST_CODE = 2
        const val READ_STORAGE_PERMISSION_CODE = 2
        const val WISATA_IMAGE: String = "Wisata_Image"
        const val WISATA: String = "wisata"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tambahWisataBinding = ActivityTambahWisataBinding.inflate(layoutInflater)
        val view = tambahWisataBinding.root
        setContentView(view)

        supportActionBar!!.hide()

        tambahWisataBinding.btnSave.setOnClickListener(this)
        tambahWisataBinding.imgWisata.setOnClickListener(this)

        tambahWisataBinding.btnBack.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.img_wisata ->{
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        imageResult(this)
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.btn_save ->{
                    if(validateAddWisataDetail()){
                        if(mSelectedImageFileUri != null){
                            showProgressDialog(resources.getString(R.string.loading))
                            uploadWisataToCloudStorage(mSelectedImageFileUri, WISATA_IMAGE)
                        }
                    }
                }
            }
        }
    }

    private fun imageResult(activity: Activity){
        val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
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
                imageResult(this)
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

                    tambahWisataBinding.imgWisata.setImageDrawable(
                            ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_baseline_add_circle_24
                            )
                    )

                    try {
                        mSelectedImageFileUri = data.data!!
                        Glide.with(this)
                                .load(Uri.parse(mSelectedImageFileUri!!.toString()))
                                .centerCrop()
                                .placeholder(R.drawable.ic_user_placeholder)
                                .into(tambahWisataBinding.imgWisata)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                                this,
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

    private fun validateAddWisataDetail(): Boolean {
        //text nya masih belom diubah
        return when {
            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.gagal_pilih_gambar), true)
                false
            }
            TextUtils.isEmpty(
                    tambahWisataBinding.etNamaWisata.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.gagal_pilih_gambar), true)
                false
            }
            TextUtils.isEmpty(
                    tambahWisataBinding.etDeskripsiWisata.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.gagal_pilih_gambar), true)
                false
            }
            TextUtils.isEmpty(
                    tambahWisataBinding.etHargaTiket.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.gagal_pilih_gambar), true)
                false
            }
            TextUtils.isEmpty(
                    tambahWisataBinding.etLokasi.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                        resources.getString(R.string.gagal_pilih_gambar), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.daftar_sukses), false)
                true
            }
        }
    }

    fun uploadWisataDetails(){
        val username = this.getSharedPreferences(
                GOESJOGJA_PREFERENCES, Context.MODE_PRIVATE)
                .getString(LOGGED_IN_USERNAME, "")!!

        val wisata = Wisata(LoginAdminActivity().getCurrentUserID(),
                username,
                tambahWisataBinding.etNamaWisata.text.toString().trim{ it <= ' ' },
                tambahWisataBinding.etDeskripsiWisata.text.toString().trim{ it <= ' '},
                tambahWisataBinding.etHargaTiket.text.toString().trim { it <= ' '},
                tambahWisataBinding.etLokasi.text.toString().trim { it <= ' '},
                mWisataImageURL

        )

        mFireStore.collection(WISATA)
                .document()
                .set(wisata, SetOptions.merge())
                .addOnSuccessListener {
                    hideProgressDialog()
                    //text belom diganti
                    Toast.makeText(this, resources.getString(R.string.daftar_sukses), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(
                            javaClass.simpleName,
                            "Error while uploading wisata details",
                            e
                    )
                }
    }

    private fun uploadWisataToCloudStorage(imageFileUri: Uri?, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                UserProfileActivity.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." + getFileExtension(imageFileUri)
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
                                mWisataImageURL = uri.toString()
                                uploadWisataDetails()
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

}