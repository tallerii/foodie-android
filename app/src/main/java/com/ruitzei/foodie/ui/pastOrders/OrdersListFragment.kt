package com.ruitzei.foodie.ui.pastOrders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.Order
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.ui.order.OrderViewModel
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_dashboard.*

class OrdersListFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel

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

        orderViewModel.claimOrderAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "claiming order")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success claiming order")
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "error claiming order")
                }
            }
        })

        orderViewModel.getUnassignedOrders()
    }

    fun showAdapter(orders: List<Order>) {
        val adapter = OrdersAdapter(orders) {
            handleOrderClick(it)
        }

        orders_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setAdapter(adapter)
        }
    }

    fun handleOrderClick(order: Order) {
        if (UserData?.user?.isDelivery == true) {
            orderViewModel.claimOrder(order)
        }
    }

    companion object {
        val TAG: String = OrdersListFragment::class.java.simpleName

        fun newInstance(): OrdersListFragment {
            return OrdersListFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
}