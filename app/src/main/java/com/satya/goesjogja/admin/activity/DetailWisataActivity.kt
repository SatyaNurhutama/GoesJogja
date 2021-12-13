package com.satya.goesjogja.admin.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityDetailWisataBinding
import com.satya.goesjogja.user.activity.LoginActivity
import com.satya.goesjogja.user.model.Cart

class DetailWisataActivity : BaseActivity(), View.OnClickListener {

    private lateinit var detailWisataBinding: ActivityDetailWisataBinding
    private var mWisataId: String = ""
    private var mWisataOwnerId: String = ""
    private lateinit var mWisata: Wisata
    private lateinit var mWisataDetails: Wisata

    private val mFireStore = FirebaseFirestore.getInstance()

    companion object{
        const val EXTRA_WISATA_ID: String = "extra_wisata_id"
        const val EXTRA_WISATA_OWNER_ID: String = "extra_wisata_admin_id"
        const val DEFAULT_ORDER_QUANTITY: String = "1"
        const val CART_ITEMS: String = "cart_items"
        const val WISATA_ID: String = "wisata_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailWisataBinding = ActivityDetailWisataBinding.inflate(layoutInflater)
        val view = detailWisataBinding.root
        setContentView(view)

        if(intent.hasExtra(EXTRA_WISATA_ID)){
            mWisataId = intent.getStringExtra(EXTRA_WISATA_ID)!!
            Log.i("Wisata Id", mWisataId)
        }
        if (intent.hasExtra(EXTRA_WISATA_OWNER_ID)){
            mWisataOwnerId = intent.getStringExtra(EXTRA_WISATA_OWNER_ID)!!
            Log.i("Wisata Owner Id", mWisataOwnerId)
        }

        if(LoginActivity().getCurrentUserID() == mWisataOwnerId){
            detailWisataBinding.btnBeli.visibility = View.GONE
        } else {
            detailWisataBinding.btnBeli.visibility = View.VISIBLE
        }

        detailWisataBinding.btnBeli.setOnClickListener(this)


        showProgressDialog(resources.getString(R.string.loading))

        getDataWisataDetail(mWisataId)

    }

    @SuppressLint("SetTextI18n")
    fun dataWisata(wisata: Wisata){
        mWisataDetails = wisata
        hideProgressDialog()
        mWisata = wisata
        Glide.with(this)
            .load(mWisata.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_placeholder)
            .into(detailWisataBinding.imgWisata)
        detailWisataBinding.tvNamaWisata.text = wisata.nama
        detailWisataBinding.tvDeskripsi.text = wisata.deskripsi
        detailWisataBinding.tvHarga.text = "Rp${wisata.harga}"
        detailWisataBinding.tvLokasi.text = wisata.lokasi
    }

    fun getDataWisataDetail(wisataId: String) {
        mFireStore.collection(TambahWisataActivity.WISATA)
            .document(wisataId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(javaClass.simpleName, document.toString())
                val wisata = document.toObject(Wisata::class.java)

                if (wisata != null) {
                    dataWisata(wisata)
                }
            }

            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e(
                    javaClass.simpleName,
                    "Error while getting wisata details.",
                    e
                )
            }
    }

    private fun addToCArt(){
        val addToCart = Cart(
                LoginActivity().getCurrentUserID(),
                mWisataOwnerId,
                mWisataId,
                mWisataDetails.nama,
                mWisataDetails.harga,
                mWisataDetails.image,
                DEFAULT_ORDER_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.loading))
        addCartItems(addToCart)

    }

    private fun addCartItems(cart: Cart){
        mFireStore.collection(CART_ITEMS)
                .document()
                .set(cart, SetOptions.merge())
                .addOnSuccessListener {
                    hideProgressDialog()
                    //ganti string
                    Toast.makeText(this, resources.getString(R.string.beli_tiket), Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    hideProgressDialog()
                    Log.e(javaClass.simpleName, "")
                }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_beli -> {
                    addToCArt()
                }
            }
        }
    }
}