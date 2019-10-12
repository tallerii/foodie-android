package com.ruitzei.foodie.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.ruitzei.foodie.MainActivity
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: BaseActivity() {

    private var viewModel: LoginViewModel? = null
    private var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.ruitzei.foodie.R.layout.activity_login)

        viewModel = viewModelProvider()

        viewModel?.loginAction?.observe(this, Observer {
            val intent = MainActivity.newIntent(context = baseContext)
            startActivity(intent)
        })

        login_button.setOnClickListener {
            viewModel?.performLogin(login_username.text.toString(), login_password.text.toString())
        }

        fb_login_btn.setOnClickListener {
//            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
            fb_login_btn_secret.performClick()
        }

        fb_login_btn_secret.setReadPermissions("public_profile", "email")
        // Callback registration
        fb_login_btn_secret.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Toast.makeText(baseContext, loginResult.accessToken.token, Toast.LENGTH_SHORT).show()
                viewModel?.performFBLogin(loginResult.accessToken.token)
            }

            override fun onCancel() {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(baseContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}