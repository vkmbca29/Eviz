package com.sanekt.eviz.dashboard

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.create_new_card.MainActivity
import com.sanekt.eviz.dashboard.fragments.HomeFragment


class DashBoardActivity : AppCompatActivity(){

    var bottomNavigation: BottomNavigationView? = null

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.logo_xl_big)

           bottomNavigation = findViewById(R.id.bottom_navigation)
           bottomNavigation!!.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
           openFragments(HomeFragment())

    }

    fun openFragments(fragment: Fragment?) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        openFragments(HomeFragment())
                        return true
                    }
                    R.id.navigation_add -> {
                        checkStoragePermission()
                        return true
                    }
                    R.id.navigation_user -> {
                        startActivity(Intent(this@DashBoardActivity, ProfileActivity::class.java))
                        return true
                    }
                }
                return false
            }
        }

    private fun checkStoragePermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val sendIntent = Intent(this@DashBoardActivity, MainActivity::class.java)
                        startActivityForResult(sendIntent,100)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            })
            .onSameThread()
            .check()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data!=null) {
            dataString= data?.getStringExtra("result").toString()
            val adapter = MyCardListAdapter(dashBoardModelList!!,this,dataString)
            MyCardList.adapter = adapter
            adapter.notifyDataSetChanged()
        }else{
            val adapter = MyCardListAdapter(dashBoardModelList!!,this,dataString)
            MyCardList.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }*/

}