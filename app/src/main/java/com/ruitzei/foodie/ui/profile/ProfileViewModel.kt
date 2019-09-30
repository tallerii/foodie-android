package com.ruitzei.foodie.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.utils.Resource

class ProfileViewModel : ViewModel() {
    val userAvatar: MutableLiveData<Resource<String>> = MutableLiveData()

    fun changeAvatar(avatar: String) {
        userAvatar?.value = Resource.loading()

        userAvatar?.value = Resource.success("https://freerangestock.com/sample/116134/businessman-avatar-.jpg")
    }
}