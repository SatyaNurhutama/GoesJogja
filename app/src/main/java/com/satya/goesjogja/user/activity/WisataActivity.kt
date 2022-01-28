package com.satya.goesjogja.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.adapter.WisataAdminAdapter
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityWisataBinding
import com.satya.goesjogja.user.adapter.WisataAdapter

class WisataActivity : BaseActivity() {

    companion object{
        const val WISATA: String = "wisata"
    }

    private lateinit var wisataBinding: ActivityWisataBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wisataBinding = ActivityWisataBinding.inflate(layoutInflater)
        val view = wisataBinding.root
        setContentView(view)

        supportActionBar?.hide()

        wisataList()
    }

    private fun wisataList() {
        showProgressDialog(resources.getString(R.string.loading))
        mFireStore.collection(WISATA)
            .get()
            .addOnSuccessListener { document ->
                Log.e(javaClass.simpleName, document.documents.toString())
                val wisataList: ArrayList<Wisata> = ArrayList()

                for (i in document.documents) {
                    val wisata = i.toObject(Wisata::class.java)
                    wisata!!.wisata_id = i.id
                    wisataList.add(wisata)
                }

                checkListWisata(wisataList)
            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e(
                    javaClass.simpleName,
                    "Error while getting dashboard items list",
                    e
                )
            }
    }

    private fun checkListWisata(list: ArrayList<Wisata>){
        hideProgressDialog()

        if(list.size > 0){
            wisataBinding.rvWisataItem.visibility = View.VISIBLE
            wisataBinding.tvKosong.visibility = View.GONE

            wisataBinding.rvWisataItem.layoutManager = LinearLayoutManager(this)
            wisataBinding.rvWisataItem.setHasFixedSize(true)
            val wisataAdapter = WisataAdapter()
            wisataAdapter.setList(list)
            wisataBinding.rvWisataItem.adapter = wisataAdapter
        } else{
            wisataBinding.rvWisataItem.visibility = View.GONE
            wisataBinding.tvKosong.visibility = View.VISIBLE
        }
    }


}