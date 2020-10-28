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
import org.json.JSONException


class LoginActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var preference: Preference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(this)

        if (preference?.isSession()!!) {
            var intent = Intent(applicationContext, DashBoardActivity::class.java)
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
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        AccessToken.getCurrentAccessToken()
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    Toast.makeText(applicationContext, "login successfully", Toast.LENGTH_SHORT)
                        .show()
                    val request =
                        GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken()
                        ) { obj, response ->
                            try {
                                var profile_name:String? = obj.get("name").toString()
                                var fb_id = obj.get("id").toString()
//                                var email_id = obj.getString("email")
//                                var gender = obj.getString("gender")
//                                if (obj.has("first_name"))
//                                    obj.getString("first_name")
//                                if (obj.has("last_name"))
//                                    obj.getString("last_name")
//                                if (obj.has("email"))
//                                    obj.getString("email")
//                                if (obj.has("gender"))
//                                    obj.getString("gender")
//                                if (obj.has("birthday"))
//                                    obj.getString("birthday")
//                                if (obj.has("location"))
//                                    obj.getJSONObject("location").getString("name")
                                var uri =
                                    "https://graph.facebook.com/$fb_id/picture?width=500&width=500"

                                preference?.set(Preference.FIRST_NAME, profile_name)
                                preference?.set(Preference.LAST_NAME, "")
                                preference?.set(Preference.EMAIL, "")
                                preference?.set(Preference.PIC_URL, "")
                                var intent = Intent(applicationContext, UserProfileActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } catch (e: JSONException) {
                                // TODO Auto-generated catch block
                                //  e.printStackTrace();
                            }
                        }

                    request.executeAsync()

                }

                override fun onCancel() {
                    // App code
                    Toast.makeText(applicationContext, "cancel", Toast.LENGTH_SHORT).show()

                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                }
            })
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
            var intent = Intent(applicationContext, UserProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, "logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

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