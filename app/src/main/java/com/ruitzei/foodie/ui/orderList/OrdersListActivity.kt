package com.ruitzei.foodie.ui.orderList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ruitzei.foodie.R
import com.ruitzei.foodie.ui.pastOrders.OrdersListFragment
import com.ruitzei.foodie.utils.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*


class OrdersListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_holder)
        setSupportActionBar(toolbar)

        startFragment(OrdersListFragment.newInstance())
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_holder, fragment)
            .commit()
    }

    companion object {
        val TAG: String = OrdersListActivity::class.java.simpleName
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, OrdersListActivity::class.java)

            return intent
        }
    }
}