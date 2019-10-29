package com.ruitzei.foodie.ui.order

import androidx.lifecycle.ViewModel
import com.ruitzei.foodie.utils.ActionLiveData

class OrderViewModel: ViewModel() {

    val orderDetailAction: ActionLiveData<String> = ActionLiveData()
    val orderAmountAction: ActionLiveData<String> = ActionLiveData()
    val addressFromAction: ActionLiveData<String> = ActionLiveData()
    val addressToAction: ActionLiveData<String> = ActionLiveData()

    val endOrderAction: ActionLiveData<String> = ActionLiveData()
}