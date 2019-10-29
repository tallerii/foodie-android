package com.ruitzei.foodie.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.BaseFragment
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_order_description.*


class OrderDescriptionFragment : BaseFragment() {

    private var viewModel: OrderViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order_btn.setOnClickListener {
            viewModel?.orderAmountAction?.sendAction("")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activityViewModelProvider()
    }

    companion object {
        val TAG: String = OrderDescriptionFragment::class.java.simpleName

        fun newInstance(): OrderDescriptionFragment {
            return OrderDescriptionFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}