package com.sanekt.eviz.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.sanekt.eviz.R
import com.sanekt.eviz.dashboard.DashBoardActivity
import com.sanekt.eviz.dashboard.UserProfileActivity
import com.sanekt.eviz.utils.Preference
import kotlinx.android.synthetic.main.login_button_layout.*


class LoginActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var preference:Preference?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference= Preference(this)

        if(preference?.isSession()!!){
            var intent=Intent(applicationContext, DashBoardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.mipmap.logo_xl_big)
    }

    override fun onStart() {
        super.onStart()
        google_login_button.setOnClickListener {
            signIn()
        }
        // [END onActivityResult]

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END build_client]
        AccessToken.getCurrentAccessToken()
        callbackManager = CallbackManager.Factory.create()
        facebook_login_button.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.i("vikram", "onsuccess")
//                TODO("Not yet implemented")
                }

                override fun onCancel() {
                    Log.i("vikram", "onCancel")

//                TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException?) {
                    val accessToken = AccessToken.getCurrentAccessToken()
                    val isLoggedIn = accessToken != null && !accessToken.isExpired
                    Log.i("vikram", "onError")
//                TODO("Not yet implemented")
                }


            })
        LoginManager.getInstance().retrieveLoginStatus(this, object : LoginStatusCallback {
            override fun onCompleted(accessToken: AccessToken) {
                Log.i("vikram", "onsuccess")
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                Log.i("vikram", "onsuccess")
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }

            override fun onFailure() {
                // No access token could be retrieved for the user
            }

            override fun onError(exception: Exception) {
                // An error occurred
            }
        })

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

    }

    // [END revokeAccess]
    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {

            val id = account.id
            var fName = account.givenName
            var lName = account.familyName
            var email = account.email
            val picUrl = if (account.photoUrl != null) {
                account.photoUrl.toString()
            } else {
                ""
            }

            if (fName == null) fName = ""

             if (lName == null) lName = ""

            preference?.set(Preference.FIRST_NAME, fName)
            preference?.set(Preference.LAST_NAME, lName)
            preference?.set(Preference.EMAIL, email)
            preference?.set(Preference.PIC_URL, picUrl)
//            Toast.makeText(applicationContext, "loggedIn successfully", Toast.LENGTH_SHORT).show()
            var intent=Intent(applicationContext, UserProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // [END handleSignInResult]
    // [START signIn]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(
                TAG, "signInResult:failed code=" + e.statusCode
            )
            updateUI(null)
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
        private const val TAG = "SignInActivity"

    }
}