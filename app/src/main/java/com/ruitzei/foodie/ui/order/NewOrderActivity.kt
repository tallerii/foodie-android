package com.ruitzei.foodie.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.viewModelProvider


class NewOrderActivity : BaseActivity() {
    private var viewmodel: OrderViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_order)
        initViewModel()

        viewmodel?.orderDetailAction?.sendAction("")
    }

    private fun initViewModel() {
        viewmodel = viewModelProvider()

        viewmodel?.addressToAction?.observe(this, androidx.lifecycle.Observer {
            startFragment(OrderAddressFragment.newInstance(
                title = "Dirección de Entrega",
                subtitle = "A donde se va a entregar el producto",
                addressType = AddressType.TO
            ))
        })

        viewmodel?.addressFromAction?.observe(this, androidx.lifecycle.Observer {
            startFragment(OrderAddressFragment.newInstance(
                title = "Dirección de Pickup",
                subtitle = "De donde vamos a levantar tu pedido.",
                addressType = AddressType.FROM
            ))

        })
        viewmodel?.orderAmountAction?.observe(this, androidx.lifecycle.Observer {
            startFragment(OrderAmountFragment.newInstance())
        })

        viewmodel?.orderDetailAction?.observe(this, androidx.lifecycle.Observer {
            startFragment(OrderDescriptionFragment.newInstance())
        })
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_holder, fragment)
            .commit()
    }

    companion object {
        val TAG: String = NewOrderActivity::class.java.simpleName
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, NewOrderActivity::class.java)

            return intent
        }
    }
}