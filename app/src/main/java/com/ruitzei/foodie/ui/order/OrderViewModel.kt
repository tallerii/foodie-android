package com.ruitzei.foodie.ui.order

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.ruitzei.foodie.application.FoodieApplication
import com.ruitzei.foodie.model.*
import com.ruitzei.foodie.service.Api
import com.ruitzei.foodie.service.RequestCallbacks
import com.ruitzei.foodie.utils.ActionLiveData
import com.ruitzei.foodie.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
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

    val ordersAction: ActionLiveData<Resource<List<OrderProperties>>> = ActionLiveData()
    val claimOrderAction: ActionLiveData<Resource<ResponseBody>> = ActionLiveData()
    val activeOrdersAction: ActionLiveData<Resource<List<OrderProperties>>> = ActionLiveData()
    val unassignedOrdersAction: ActionLiveData<Resource<List<OrderProperties>>> = ActionLiveData()

    val orderFinishedAction: ActionLiveData<Boolean> = ActionLiveData()

    init {
        order.value = OrderPostObject()
    }

    fun setDescription(description: String) {
        order.value?.description = description
    }

    fun setAmount(amount: String) {
        order.value?.amount = amount
    }

    fun setAddressFrom(addressFrom: Address, stringAddress: String) {
        order.value?.addressFrom = addressFrom
        order.value?.startAddress = stringAddress
    }

    fun setAddressTo(addressTo: Address, stringAddress: String) {
        order.value?.addressTo = addressTo
        order.value?.endAddress= stringAddress
    }

    fun endOrder() {
        order.value?.let {
            endOrderAction.sendAction(it)
        }
    }

    fun claimOrder(order: OrderProperties) {
        claimOrderAction.sendAction(Resource.loading())

        Api.claimOrder(order.id, object : RequestCallbacks<ResponseBody> {
            override fun onSuccess(response: ResponseBody) {
                claimOrderAction.sendAction(Resource.success(response))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                claimOrderAction.sendAction(Resource.error(listOf(), null))
            }
        })
    }

    fun getOrders() {
        ordersAction.sendAction(Resource.loading(null))

        Api.getOrders(object : RequestCallbacks<OrderPage> {
            override fun onSuccess(response: OrderPage) {
                ordersAction.sendAction(Resource.success(response.getOrders()))

            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                ordersAction.sendAction(Resource.error(listOf(), null))
            }
        })
    }

    fun getActiveOrders() {
        activeOrdersAction.sendAction(Resource.loading(null))

        Api.getActiveOrders(object : RequestCallbacks<OrderPage> {
            override fun onSuccess(response: OrderPage) {
                activeOrdersAction.sendAction(Resource.success(response.getOrders()))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                activeOrdersAction.sendAction(Resource.error(listOf(), null))
            }
        })
    }

    fun getUnassignedOrders() {
        unassignedOrdersAction.sendAction(Resource.loading(null))

        Api.getUnassignedOrders(object : RequestCallbacks<OrderPage> {
            override fun onSuccess(response: OrderPage) {
                unassignedOrdersAction.sendAction(Resource.success(response.getOrders()))
            }

            override fun onFailure(error: String?, code: Int, t: Throwable) {
                unassignedOrdersAction.sendAction(Resource.error(listOf(), null))
            }
        })
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