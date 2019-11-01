package com.ruitzei.foodie.ui.order

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ruitzei.foodie.application.FoodieApplication
import com.ruitzei.foodie.model.Address
import com.ruitzei.foodie.model.Order
import com.ruitzei.foodie.model.OrderPostObject
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import com.ruitzei.foodie.utils.ActionLiveData
import com.ruitzei.foodie.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class OrderViewModel: ViewModel() {

    val orderDetailAction: ActionLiveData<String> = ActionLiveData()
    val orderAmountAction: ActionLiveData<String> = ActionLiveData()
    val addressFromAction: ActionLiveData<String> = ActionLiveData()
    val addressToAction: ActionLiveData<String> = ActionLiveData()

    val addressAction: ActionLiveData<Resource<Address>> = ActionLiveData()

    val endOrderAction: ActionLiveData<OrderPostObject> = ActionLiveData()
    val createOrderAction: ActionLiveData<Resource<Order>> = ActionLiveData()

    val order: MutableLiveData<OrderPostObject> = MutableLiveData()

    init {
        order.value = OrderPostObject()
    }

    fun setDescription(description: String) {
        order.value?.description = description
    }

    fun setAmount(amount: String) {
        order.value?.amount = amount
    }

    fun setAddressFrom(addressFrom: Address) {
        order.value?.addressFrom = addressFrom
    }

    fun setAddressTo(addressTo: Address) {
        order.value?.addressTo = addressTo
    }

    fun endOrder() {
        order.value?.let {
            endOrderAction.sendAction(it)
        }
    }

    fun createOrder(order: OrderPostObject) {
        createOrderAction.sendAction(Resource.loading())

        Api.createOrder(order, object : RequestCallbacks<Order> {
            override fun onSuccess(response: Order) {
                createOrderAction.sendAction(Resource.success(response))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                createOrderAction.sendAction(Resource.error(listOf(), null))
            }
        })
    }

    fun getLocationFromAddress(strAddress: String) {
        viewModelScope.launch {
            val coder = Geocoder(FoodieApplication.instance!!.applicationContext)

            val address: List<android.location.Address>?

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    Log.d("ViewModel", "Found no address matching")
                }
                val location = address[0]

                val latLng = LatLng(location.latitude, location.longitude)

                viewModelScope.launch(Dispatchers.Main) {
                    addressAction.sendAction(
                        Resource.success(
//                            Address(
//                                address = strAddress,
//                                point = latLng.toPoint()
//                            )
                            Address(
                                 coordinates = listOf(latLng.latitude, latLng.longitude)
                            )
                        )
                    )
                }

            } catch (ex: IOException) {

                ex.printStackTrace()
                viewModelScope.launch(Dispatchers.Main) {
                    addressAction.sendAction(
                        Resource.error(
                            listOf(), null
                        )
                    )
                }
            }
        }
    }

}