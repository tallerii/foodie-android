package com.ruitzei.foodie.ui.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.Order
import com.ruitzei.foodie.model.OrderProperties
import com.ruitzei.foodie.model.User
import com.ruitzei.foodie.ui.chat.ChatActivity
import com.ruitzei.foodie.ui.order.OrderViewModel
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.activityViewModelProvider
import com.ruitzei.foodie.utils.toStringWithTwoDecimals
import kotlinx.android.synthetic.main.layout_order_detail.view.*

class OrderDetailBottomSheet: BaseBottomSheet() {
    private var order: OrderProperties? = null
    private var isDelivery: Boolean = false
    private var isDetail: Boolean = false
    private var viewModel: OrderViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            order = it.getParcelable("order")
            isDelivery = it.getBoolean("isDelivery")
            isDetail = it.getBoolean("isDetail")
        }

        viewModel = activityViewModelProvider()

        viewModel?.claimOrderAction?.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success")
                    viewModel?.orderFinishedAction?.sendAction(true)
                    dismissAllowingStateLoss()
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "Error")
                }
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.layout_order_detail

    override fun onChildContentViewCreated() {
        val user: User?
        val text: String
        if (isDelivery) {
            user = order?.clientUser
            text = "Entregar a ${user?.fullName}`"
        } else {
            user = order?.deliveryUser
            text = "Tu pedido lo trae ${user?.fullName}`"
        }

        val total = order?.price
        val deliveryPrice = order?.deliveryPrice
        val notes = order?.notes

        val startLocation = order?.startLocation?.coordinates
        val endLocation = order?.endLocation?.coordinates


        childContentView.detail_user_name.text = text
        childContentView.detail_pickup_address.text = startLocation.toString()
        childContentView.detail_delivery_address.text = endLocation.toString()
        childContentView.detail_notes.text = notes
        childContentView.detail_price.text = total?.toStringWithTwoDecimals()
        childContentView.detail_delivery_price.text = deliveryPrice?.toStringWithTwoDecimals()

        childContentView.detail_order_finish.setOnClickListener {
            order?.let {
                viewModel?.claimOrder(order!!)
            }
        }

        childContentView.detail_open_chat.setOnClickListener {
            order?.let {
                startActivity(ChatActivity.newIntent(context!!, it.id))
            }
        }

        childContentView.detail_take_order.setOnClickListener {
            order?.let {
                viewModel?.claimOrder(order!!)
            }
        }

        if (isDetail) {
            childContentView.detail_take_order.visibility = View.VISIBLE
            childContentView.detail_open_chat.visibility = View.GONE
            childContentView.detail_order_finish.visibility = View.GONE
        }
    }

    override fun getAvatarUrl(): String? = null

    override fun getTitle(): String? = "Detalle del pedido"

    companion object {
        val TAG: String = OrderDetailBottomSheet::class.java.simpleName

        fun newInstance(order: OrderProperties, isDelivery: Boolean, isDetail: Boolean = false): OrderDetailBottomSheet {
            return OrderDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable("order", order)
                    putBoolean("isDelivery", isDelivery)
                    putBoolean("isDetail", isDetail)
                }
            }
        }
    }
}