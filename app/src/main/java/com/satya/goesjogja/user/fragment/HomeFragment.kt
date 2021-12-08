package com.satya.goesjogja.user.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.FragmentHomeBinding
import com.satya.goesjogja.user.activity.WisataActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        homeBinding.tvWisata.setOnClickListener(this)
        return homeBinding.root
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tv_wisata ->
                    startActivity(Intent(activity, WisataActivity::class.java))
            }
        }
    }
}