package com.sanekt.eviz.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sanekt.eviz.R
import com.sanekt.eviz.login.ui.LoginActivity
import com.sanekt.eviz.utils.Preference
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class ProfileActivity : AppCompatActivity() {
    var data="hello"
    var preference:Preference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        preference= Preference(this)
        termsId.setOnClickListener {
            var da=getTermsString()
            var intent = Intent(this, ShowTextActivity::class.java)
            intent.putExtra("text",da)
            startActivity(intent)
        }
        privacyId.setOnClickListener {
            var intent = Intent(this, ShowTextActivity::class.java)
            intent.putExtra("text", resources.getString(R.string.privacy_text))
            startActivity(intent)
        }
        logoutId.setOnClickListener {
            preference!!.prefClear()
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private fun getTermsString(): String? {
        val termsString = StringBuilder()
        val reader: BufferedReader
        try {
            reader = BufferedReader(
                InputStreamReader(assets.open("terms.txt"))
            )
            var str: String?=""
            while (reader.readLine().also({ str = it }) != null) {
                termsString.append(str)
            }
            reader.close()
            return termsString.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}