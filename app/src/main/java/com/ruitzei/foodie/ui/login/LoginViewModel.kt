package com.ruitzei.foodie.ui.login

import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.utils.ActionLiveData

class LoginViewModel : ViewModel() {

    val loginAction: ActionLiveData<User> = ActionLiveData()

    fun performLogin(username: String, password: String) {
        loginAction.sendAction(User())
    }
}