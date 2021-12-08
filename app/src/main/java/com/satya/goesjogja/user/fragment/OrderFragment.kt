package com.satya.goesjogja.user.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.FragmentOrderBinding
import com.satya.goesjogja.user.activity.CartActivity

class OrderFragment : Fragment() {

    private lateinit var orderBinding: FragmentOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        orderBinding = FragmentOrderBinding.inflate(layoutInflater, container, false)

        return orderBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cart -> {
                startActivity(Intent(activity, CartActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}