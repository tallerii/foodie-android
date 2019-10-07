package com.ruitzei.foodie.ui.home

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.LatLong
import com.ruitzei.foodie.model.LocationPermission
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.BaseFragment
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.viewModelProvider

class HomeFragment : BaseFragment(), OnMapReadyCallback {

    val ZOOM_LEVEL = 15.5f
    var mLocationRequest: LocationRequest? = null

    var fused: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null
    private lateinit var homeViewModel: HomeViewModel

    private var map: GoogleMap? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fused = LocationServices.getFusedLocationProviderClient(activity!!)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.d(TAG, "On location result")
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.d(TAG, "Showing my location")
                    // TODO: DO SOMETHING WITH LOCATION

                    showMyLocation(location)
                }
            }
        }

        showMap()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel = viewModelProvider()

        homeViewModel.updateLatLongAction.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "Loading")
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "Success")
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "Error")
                }
            }
        })

        homeViewModel.updateLatLong(LatLong(2.0, 2.5))
    }

    fun showMyLocation(location: Location) {
        val point = LatLng(location.latitude, location.longitude)
        map?.addMarker(MarkerOptions().position(point))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, ZOOM_LEVEL))

        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(point,
                ZOOM_LEVEL))
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled= true

        // Do something with markers
//        map.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
//        map.setOnInfoWindowClickListener { marker ->
//            val store = Gson().fromJson(marker.snippet, Store::class.java)
//            startActivity(RestaurantDetailActivity.newIntent(this, store))
//        }

        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(120000) // two minute interval
        mLocationRequest!!.setFastestInterval(120000)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

        if (activity is BaseActivity) {
            Log.d(TAG, "IS base activity")
            (activity as BaseActivity).performPermissionAction(startLocationUpdates, LocationPermission())
        }
    }

    fun showMap() {
        val map = SupportMapFragment.newInstance()
        map?.getMapAsync(this)
        childFragmentManager.beginTransaction().replace(R.id.map_holder, map).commit()
    }

    @SuppressWarnings("MissingPermission")
    val startLocationUpdates: () -> Unit = {
        fused?.requestLocationUpdates(mLocationRequest,
            locationCallback,
            Looper.myLooper())
    }


    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }
}