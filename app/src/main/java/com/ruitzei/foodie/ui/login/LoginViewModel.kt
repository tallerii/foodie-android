package com.ruitzei.foodie.ui.login

import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.application.FoodieApplication
import com.ruitzei.foodie.model.LoginResponse
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import com.ruitzei.foodie.service.RestClient
import com.ruitzei.foodie.utils.ActionLiveData

class LoginViewModel : ViewModel() {

    val loginAction: ActionLiveData<User> = ActionLiveData()

    fun performLogin(username: String, password: String) {
        Api.performLogin(username, password, object : RequestCallbacks<LoginResponse> {
            override fun onSuccess(response: LoginResponse) {
                loginWithAppToken(response.token)
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                // TODO: ERROR
            }
        })
    }

    fun performFBLogin(token: String, fcmToken: String) {
        Api.performFacebookLogin(token, fcmToken, object : RequestCallbacks<LoginResponse> {
            override fun onSuccess(response: LoginResponse) {
                loginWithAppToken(response.token)
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                // TODO: ERROR
            }
        })
    }

    private fun loginWithAppToken(token: String) {
        UserData.token = token
        FoodieApplication.instance?.api = RestClient.createPublicApi()

        Api.getLoggedInUserData( object : RequestCallbacks<User> {
            override fun onSuccess(response: User) {
                UserData.user = response

                loginAction.sendAction(User())
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                // TODO: DO something
            }
        })
    }
}