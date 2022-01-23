package com.satya.goesjogja.admin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.satya.goesjogja.R
import com.satya.goesjogja.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dashboardBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = dashboardBinding.root
        setContentView(view)

//        supportActionBar!!.hide()

        dashboardBinding.tvWisata.setOnClickListener(this)
        dashboardBinding.tvOrder.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_wisata_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_wisata -> {
                val intent = Intent(this, TambahWisataActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.tvWisata -> {
                    val intent = Intent(this, ListWisataAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.tvOrder -> {
                    val intent = Intent(this, OrderAdminActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}