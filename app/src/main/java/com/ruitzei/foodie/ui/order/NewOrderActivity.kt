package com.ruitzei.foodie.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.ruitzei.foodie.utils.BaseActivity
import java.util.*


class NewOrderActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.ruitzei.foodie.R.layout.activity_new_order)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(com.ruitzei.foodie.R.id.autocomplete_fragment) as AutocompleteSupportFragment?

// Specify the types of place data to return.
        autocompleteFragment?.setCountry("ARG")
        autocompleteFragment!!.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG))


// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.name + ", " + place.id)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    companion object {
        val TAG: String = NewOrderActivity::class.java.simpleName
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, NewOrderActivity::class.java)

            return intent
        }
    }
}