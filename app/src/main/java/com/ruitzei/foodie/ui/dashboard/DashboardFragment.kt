package com.ruitzei.foodie.ui.dashboard

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
import com.ruitzei.foodie.ui.home.HomeFragment
import com.ruitzei.foodie.ui.order.OrderViewModel
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

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

        orderViewModel.activeOrdersAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(HomeFragment.TAG, "Loading")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(HomeFragment.TAG, "Success")
                    showAdapter(it.data.orEmpty())
                }
                Resource.Status.ERROR -> {
                    Log.d(HomeFragment.TAG, "Error")
                }
            }
        })

        orderViewModel.getActiveOrders()
    }

    fun showAdapter(orders: List<Order>) {
        val adapter = OrdersAdapter(orders) {
            Log.d(TAG, "Order selected ${it.properties?.notes}")
        }

        orders_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setAdapter(adapter)
        }
    }

    companion object {
        val TAG: String = DashboardFragment::class.java.simpleName

        fun newInstance(): DashboardFragment {
            return DashboardFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
}