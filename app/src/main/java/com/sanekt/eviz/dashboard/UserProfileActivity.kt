package com.sanekt.eviz.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sanekt.eviz.R
import com.sanekt.eviz.login.ui.LoginActivity
import com.sanekt.eviz.utils.Preference
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    private var preference: Preference? = null

    private var ibBack: ImageButton? = null

    private var ivProfileImage: ImageView? = null

    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etMobileNo: EditText? = null

    private var tvEmail: TextView? = null

    companion object{
       const val REQUEST_PHONE_NUMBER = 3125
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        preference = Preference(this)

        ibBack = findViewById(R.id.ib_back)
        ivProfileImage = findViewById(R.id.iv_profile_image)
        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etMobileNo = findViewById(R.id.et_mobile_no)

        tvEmail = findViewById(R.id.tv_email)

        etMobileNo!!.setOnFocusChangeListener { _, hasFocus -> if(hasFocus) requestForMobileNumbers() }

        setData()

        ibBack!!.setOnClickListener { onBackPressed() }
        save_btn.setOnClickListener{
            var data=etMobileNo?.text.toString()
            if(!TextUtils.isEmpty(data)){
                goToDashBoardActivity()
            }else{
                Toast.makeText(applicationContext, "Please enter your mobile number.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestForMobileNumbers(){
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val pendingIntent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(pendingIntent.intentSender, REQUEST_PHONE_NUMBER, null,
            0, 0, 0)
        } catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHONE_NUMBER && resultCode == RESULT_OK) {
            var mobileNo = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)?.id
            if (mobileNo != null) {
                etMobileNo!!.setText(mobileNo.replace("+91", ""))

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        goToDashBoardActivity()
        finish()
        preference?.prefClear()
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

    private fun setData(){

        etFirstName!!.setText(preference?.get(Preference.FIRST_NAME))
        etLastName!!.setText(preference?.get(Preference.LAST_NAME))
        tvEmail!!.text = preference?.get(Preference.EMAIL)

        if(!preference?.get(Preference.PIC_URL).equals("")){
            Glide.with(this)
                .load(preference?.get(Preference.PIC_URL))
                .centerCrop()
                .placeholder(R.drawable.profile_image_placeholder)
                .into(ivProfileImage!!)
        }

    }

    private fun goToDashBoardActivity() {
        preference?.setSession(true)
        var intent=Intent(applicationContext, DashBoardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}