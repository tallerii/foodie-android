package com.ruitzei.foodie.ui.home

import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.LatLong
import com.ruitzei.foodie.model.LocationPermission
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.model.UserProperties
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.BaseFragment
import com.ruitzei.foodie.utils.Resource
import com.ruitzei.foodie.utils.viewModelProvider

class HomeFragment : BaseFragment(), OnMapReadyCallback, ValueEventListener {

    val ZOOM_LEVEL = 15.5f
    var mLocationRequest: LocationRequest? = null

    var fused: FusedLocationProviderClient? = null
    var locationCallback: LocationCallback? = null
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var usersReference: DatabaseReference

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

        initializeFirebaseTable()

        createIfNotExists()
        listenToUserChanges()
    }

    private fun initializeFirebaseTable() {
        usersReference = FirebaseDatabase.getInstance().reference
            .child("users")
    }

    private fun createIfNotExists() {
        val userId = uid

        usersReference.child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    val user = dataSnapshot.getValue(UserProperties::class.java)

                    // [START_EXCLUDE]
                    if (user == null) {
                        // User does not exist
                        Log.e(TAG, "User $userId is unexpectedly null")
                        Toast.makeText(context,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show()

                        createNewUser()
                    } else {
                    }
                    // [END_EXCLUDE]
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
                }
            })
    }

    private fun createNewUser() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        val key = uid

        val user = UserData.user
        val postValues = user!!.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/$key"] = postValues

        usersReference.updateChildren(childUpdates)
    }

    private fun listenToUserChanges() {
        usersReference.addValueEventListener(this)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        Log.w("Home", "loadPost:onCancelled", databaseError.toException())
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        map?.clear()

        for (postSnapshot in dataSnapshot.children) {
            val userData = postSnapshot.getValue(UserProperties::class.java)
            map?.addMarker(
                MarkerOptions().position(
                    LatLng(
                        userData?.lat ?: 0.0,
                        userData?.long ?: 0.0
                    )
                )
                    .title("${userData?.name} ${userData?.lastName}")
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        usersReference.removeEventListener(this)
    }

    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid


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

    fun postUser() {
        val uid = uid

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled= true

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