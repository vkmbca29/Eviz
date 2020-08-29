package com.sanekt.eviz.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.DashBoardActivity
import kotlinx.android.synthetic.main.login_button_layout.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.logo_xl_big)
    }

    override fun onStart() {
        super.onStart()
        facebook_login_button.setOnClickListener {
            startActivity(Intent(applicationContext,DashBoardActivity::class.java))
        }
        google_login_button.setOnClickListener {
            startActivity(Intent(applicationContext,DashBoardActivity::class.java))
        }
    }
}