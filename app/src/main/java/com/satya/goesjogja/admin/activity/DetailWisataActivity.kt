package com.satya.goesjogja.admin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityDetailWisataBinding
import com.satya.goesjogja.user.activity.CartActivity
import com.satya.goesjogja.user.activity.LoginActivity
import com.satya.goesjogja.user.model.Cart

class DetailWisataActivity : BaseActivity(), View.OnClickListener {

    private lateinit var detailWisataBinding: ActivityDetailWisataBinding
    private var mWisataId: String = ""
    private var mWisataOwnerId: String = ""
    private lateinit var mWisata: Wisata
    private lateinit var mWisataDetails: Wisata
    var mapView: MapView? = null

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
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        detailWisataBinding = ActivityDetailWisataBinding.inflate(layoutInflater)
        val view = detailWisataBinding.root
        setContentView(view)

        supportActionBar?.hide()

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


        mapView = detailWisataBinding.mapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            it.setStyle(Style.MAPBOX_STREETS)
                val location = LatLng(-7.796276005979423, 110.36832102838014)
                val position = CameraPosition.Builder()
                    .target(LatLng(-7.796276005979423, 110.36832102838014))
                    .zoom(10.0)
                    .tilt(10.0)
                    .bearing(5.0)
                    .build()

            it.animateCamera(CameraUpdateFactory.newCameraPosition(position), 5000)
            it.addMarker(MarkerOptions().setPosition(location))
        }

        detailWisataBinding.navMaps.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.id/maps/place/Yogyakarta,+Kota+Yogyakarta,+Daerah+Istimewa+Yogyakarta/@-7.8015586,110.3476768,12.5z/data=!4m5!3m4!1s0x2e7a5787bd5b6bc5:0x21723fd4d3684f71!8m2!3d-7.7955798!4d110.3694896?hl=id"))
            startActivity(intent)
        }

        detailWisataBinding.btnBack.setOnClickListener{
            onBackPressed()
        }

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
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}