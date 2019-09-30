package com.ruitzei.foodie.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ruitzei.foodie.MainActivity
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: BaseActivity() {

    private var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        viewModel = viewModelProvider()

        viewModel?.loginAction?.observe(this, Observer {
            val intent = MainActivity.newIntent(context = baseContext)
            startActivity(intent)
        })

        login_button.setOnClickListener {
            viewModel?.performLogin(login_username.text.toString(), login_password.text.toString())
        }
    }
}