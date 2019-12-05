package com.ruitzei.foodie.ui.pastOrders

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.OrderProperties
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.ui.bottomsheet.OrderDetailBottomSheet
import com.ruitzei.foodie.ui.modal.RatingModal
import com.ruitzei.foodie.ui.order.OrderViewModel
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_dashboard.*

class OrdersListFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var showsOld: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showsOld = arguments!!.getBoolean("old")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        orderViewModel = activityViewModelProvider()

        orderViewModel.unassignedOrdersAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success")
                    showAdapter(it.data.orEmpty())
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "Error")
                }
            }
        })

        orderViewModel.ordersAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success")
                    showAdapter(it.data.orEmpty().filter { it?.clientUser?.id == UserData.user?.id || it?.deliveryUser?.id == UserData.user?.id })
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "Error")
                }
            }
        })

        orderViewModel.claimOrderAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "claiming order")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success claiming order")
                    Toast.makeText(context, "Pedido asignado", Toast.LENGTH_SHORT).show()

                    activity?.setResult(RESULT_OK)
                    activity?.finish()
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "error claiming order")
                    Toast.makeText(context, "Error asignando pedido", Toast.LENGTH_SHORT).show()
                }
            }
        })


        if (showsOld) {
            orderViewModel.getOrders()
        } else {
            orderViewModel.getUnassignedOrders()
        }
    }

    fun showAdapter(orders: List<OrderProperties>) {
        val adapter = OrdersAdapter(orders) {
            handleOrderClick(it)
        }

        orders_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setAdapter(adapter)
        }
    }

    fun handleOrderClick(order: OrderProperties) {
        if (showsOld && UserData.user?.isDelivery == false) {
            if (order.reviews.isEmpty()) {
                RatingModal.newInstance(
                    ratingModel = null,
                    orderId = order.id
                ).show(childFragmentManager, "modal")
            } else {
                RatingModal.newInstance(
                    ratingModel = order.reviews.first(),
                    orderId = order.id
                ).show(childFragmentManager, "modal")
            }
        } else if (UserData?.user?.isDelivery == true && !showsOld) {
            OrderDetailBottomSheet.newInstance(order, true, true).show(childFragmentManager, "")
        }
    }

    companion object {
        val TAG: String = OrdersListFragment::class.java.simpleName

        fun newInstance(showsOld: Boolean = false): OrdersListFragment {
            return OrdersListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("old", showsOld)
                }
            }
        }
    }
}