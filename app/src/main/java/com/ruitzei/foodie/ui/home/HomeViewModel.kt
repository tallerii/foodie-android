package com.ruitzei.foodie.ui.home

import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.model.LatLong
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import com.ruitzei.foodie.service.ServerError
import com.ruitzei.foodie.utils.ActionLiveData
import com.ruitzei.foodie.utils.Resource

class HomeViewModel : ViewModel() {
    val updateLatLongAction: ActionLiveData<Resource<User>> = ActionLiveData()
    val openOrdersListAction: ActionLiveData<Boolean> = ActionLiveData()

    fun updateLatLong(latLong: LatLong) {
        updateLatLongAction?.sendAction(Resource.loading())
        Api.updateLatLong(latLong, object : RequestCallbacks<User> {
            override fun onSuccess(response: User) {
                updateLatLongAction?.sendAction(Resource.success(response))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                updateLatLongAction?.sendAction(Resource.error(listOf(ServerError()), null))
            }
        })
    }
}