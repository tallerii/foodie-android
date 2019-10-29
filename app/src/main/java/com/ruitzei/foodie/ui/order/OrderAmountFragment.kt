package com.ruitzei.foodie.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.BaseFragment
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_order_price.*


class OrderAmountFragment : BaseFragment() {

    private var viewModel: OrderViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        order_price_btn.setOnClickListener {
            viewModel?.addressFromAction?.sendAction("")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activityViewModelProvider()
    }

    companion object {
        val TAG: String = OrderAmountFragment::class.java.simpleName

        fun newInstance(): OrderAmountFragment {
            return OrderAmountFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}