package com.ruitzei.foodie.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import com.ruitzei.foodie.service.ServerError
import com.ruitzei.foodie.utils.ActionLiveData
import com.ruitzei.foodie.utils.Resource

class ProfileViewModel : ViewModel() {
    val userAvatar: MutableLiveData<Resource<String>> = MutableLiveData()
    val userUpdateResponse: ActionLiveData<Resource<User>> = ActionLiveData()

    fun changeAvatar(avatar: String) {
        userAvatar?.value = Resource.loading()

        userAvatar?.value = Resource.success("https://freerangestock.com/sample/116134/businessman-avatar-.jpg")
    }

    fun updateFields(name: String, phoneNumber: String, mail: String) {
        userUpdateResponse.sendAction(Resource.loading())
        Api.updateUserData(name, phoneNumber, mail, object : RequestCallbacks<User> {
            override fun onSuccess(response: User) {
                userUpdateResponse.sendAction(Resource.success(response))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                userUpdateResponse.sendAction(Resource.error(listOf(ServerError()), null))
            }
        })
    }
}