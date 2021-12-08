package com.satya.goesjogja.admin.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.satya.goesjogja.BaseActivity
import com.satya.goesjogja.R
import com.satya.goesjogja.admin.adapter.WisataAdminAdapter
import com.satya.goesjogja.admin.model.Wisata
import com.satya.goesjogja.databinding.ActivityListWisataAdminBinding
import com.satya.goesjogja.user.activity.UserProfileActivity

class ListWisataAdminActivity : BaseActivity() {

    private lateinit var binding: ActivityListWisataAdminBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    companion object{
        const val ADMIN_ID :String ="admin_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListWisataAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getWisataList()
    }

    private fun getWisataList() {
        showProgressDialog(resources.getString(R.string.loading))
        mFireStore.collection(TambahWisataActivity.WISATA)
            .whereEqualTo(ADMIN_ID, LoginAdminActivity().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Wisata List", document.documents.toString())
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
                            "Error while getting user details.",
                            e
                    )
                }
    }

    private fun checkListWisata(list: ArrayList<Wisata>){
        hideProgressDialog()

        if(list.size > 0){
            binding.rvWisataItem.visibility = View.VISIBLE
            binding.tvKosong.visibility = View.GONE

            binding.rvWisataItem.layoutManager = LinearLayoutManager(this)
            binding.rvWisataItem.setHasFixedSize(true)
            val wisataAdapter = WisataAdminAdapter(this)
            wisataAdapter.setList(list)
            binding.rvWisataItem.adapter = wisataAdapter
        } else{
            binding.rvWisataItem.visibility = View.GONE
            binding.tvKosong.visibility = View.VISIBLE
        }
    }

    fun alertDeleteWisata(wisataId: String){
        //ganti text nya buat hapus item
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.gagal_pilih_gambar))
        builder.setMessage(resources.getString(R.string.gagal_pilih_gambar))
        builder.setIcon(android.R.drawable.ic_delete)

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialogInterface, _ ->
            dialogInterface.dismiss()
            showProgressDialog(resources.getString(R.string.loading))

            mFireStore.collection(TambahWisataActivity.WISATA)
                    .document(wisataId)
                    .delete()
                    .addOnSuccessListener {
                        hideProgressDialog()
                        Toast.makeText(this, resources.getString(R.string.daftar_sukses), Toast.LENGTH_SHORT).show()
                        getWisataList()
                    }

                    .addOnFailureListener { e ->
                        hideProgressDialog()
                        Log.e(
                                javaClass.simpleName,
                                "Error while deleting the wisata",
                                e
                        )
                    }

        }
        builder.setNegativeButton(resources.getString(R.string.tidak)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}