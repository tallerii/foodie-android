package com.ruitzei.foodie.ui.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.gms.maps.model.LatLng
import com.ruitzei.foodie.R
import com.ruitzei.foodie.utils.BaseFragment
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.activityViewModelProvider
import kotlinx.android.synthetic.main.fragment_order_address.*
import java.util.*


class OrderAddressFragment : BaseFragment() {
    private var title: String = "Dirección de Entrega"
    private var subtitle: String = "A donde se va a entregar el producto"
    private var addressType: AddressType = AddressType.TO

    private var latLng: LatLng? = null

    private var viewModel: OrderViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(TITLE, "")
            subtitle= it.getString(SUBTITLE, "")
            addressType = it.getSerializable(ADRESS_TYPE) as AddressType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        address_title.text = title
        address_subtitle.text = subtitle

        val adapter = ArrayAdapter<String>(context!!,
            android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.localities))

        address_localities.setAdapter<ArrayAdapter<String>>(adapter)
        address_localities.validator = Validator()
        address_localities.setOnFocusChangeListener { view, b ->
            if (!b) {
                (view as AutoCompleteTextView).performValidation()
            }
        }


        order_address_btn.setOnClickListener {
            fetchAddress()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel= activityViewModelProvider()

        viewModel?.addressAction?.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "Observer")

            when (it.status) {
                Resource.Status.LOADING-> {

                }
                Resource.Status.SUCCESS -> {
                    val address = getGoogleMapsAddress()

                    if (addressType == AddressType.FROM) {
                        viewModel?.setAddressFrom(
                            it.data!!,
                            address_address.text.toString()
                        )

                        viewModel?.addressToAction?.sendAction("")
                    } else {
                        viewModel?.setAddressTo(
                            it.data!!,
                            address_address.text.toString()
                        )
                        viewModel?.endOrder()
                    }
                }
                Resource.Status.ERROR -> {

                }
            }
        })
    }

    private fun fetchAddress() {
        viewModel?.getLocationFromAddress(getGoogleMapsAddress())
    }

    fun getGoogleMapsAddress(): String {
        Log.d(TAG, "${address_address.text}, ${address_localities.text}, Ciudad Autónoma de Buenos Aires")
        return "${address_address.text}, ${address_localities.text}, Ciudad Autónoma de Buenos Aires"
    }

    internal inner class Validator : AutoCompleteTextView.Validator {
        override fun isValid(text: CharSequence): Boolean {
            val array = resources.getStringArray(R.array.localities)
            Log.v("Test", "Checking if valid: " + text)
            return (Arrays.binarySearch(array, text.toString()) > 0)
        }

        override fun fixText(invalidText: CharSequence): CharSequence {
            Log.v("Test", "Returning fixed text")

            /* I'm just returning an empty string here, so the field will be blanked,
             * but you could put any kind of action here, like popping up a dialog?
             *
             * Whatever value you return here must be in the list of valid words.
             */
            return ""
        }
    }

    companion object {
        val TAG: String = OrderAddressFragment::class.java.simpleName
        val TITLE: String = "Title"
        val SUBTITLE: String = "SubTitle"
        val ADRESS_TYPE: String = "addressType"

        fun newInstance(title: String, subtitle: String, addressType: AddressType = AddressType.FROM): OrderAddressFragment {
            return OrderAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(SUBTITLE, subtitle)
                    putSerializable(ADRESS_TYPE, addressType)
                }
            }
        }
    }
}

enum class AddressType {
    FROM,
    TO
}