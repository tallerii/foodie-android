package com.ruitzei.foodie.ui.login

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.ruitzei.foodie.MainActivity
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.PreferenceManager
import com.ruitzei.foodie.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity: BaseActivity() {

    private var viewModel: LoginViewModel? = null
    private var callbackManager = CallbackManager.Factory.create()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.ruitzei.foodie.R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        viewModel = viewModelProvider()

        viewModel?.loginAction?.observe(this, Observer {
            val intent = MainActivity.newIntent(context = baseContext)
            startActivity(intent)
        })

        login_button.setOnClickListener {
            firebaseLogin()
        }

        fb_login_btn.setOnClickListener {
            // User might already be logged in, so need to check it.
            if (isLoggedIn()) {
                val accessToken = AccessToken.getCurrentAccessToken()
                handleFacebookAccessToken(accessToken)
            } else {
                fb_login_btn_secret.performClick()
            }
        }

        fb_login_btn_secret.setReadPermissions("public_profile", "email")
        // Callback registration
        fb_login_btn_secret.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Toast.makeText(baseContext, loginResult.accessToken.token, Toast.LENGTH_SHORT).show()
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(baseContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        tryToLogin()

        val info: PackageInfo
        try {
            info = packageManager.getPackageInfo("com.ruitzei.foodie", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hash = String(Base64.encode(md.digest(), 0))
                Log.e("hash", hash)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }

    }

    private fun tryToLogin() {
        // Checking if the user has already logged
        val token = PreferenceManager.getToken()

        if (token.isNotEmpty() && isLoggedIn()) {
            val accessToken = AccessToken.getCurrentAccessToken()
            handleFacebookAccessToken(accessToken)

            Toast.makeText(this, "Ya estaba logueado", Toast.LENGTH_SHORT).show()
        }
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

    private fun handleFacebookAccessToken(accesToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accesToken.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    getFirebaseInstanceID {
                        viewModel?.performFBLogin(accesToken.token, it)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                }
            }
    }

    // Logs in to the application (logging in to firebase before, if it has a facebook access token)
    private fun firebaseLogin() {
        val token = AccessToken.getCurrentAccessToken()
        if (token != null) {
            val credential = FacebookAuthProvider.getCredential(token.token)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Login", "signInWithCredential:success")
                    } else {
                        Log.w("Login", "signInWithCredential:failure", task.exception)
                    }

                    viewModel?.performLogin(login_username.text.toString(), login_password.text.toString())
                }
        }
    }

    private fun getFirebaseInstanceID(success: (String) -> Unit) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(MainActivity.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token.orEmpty()
                success(token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}