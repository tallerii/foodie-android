package com.ruitzei.foodie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.ruitzei.foodie.model.UserData
import com.ruitzei.foodie.ui.home.HomeViewModel
import com.ruitzei.foodie.ui.order.NewOrderActivity
import com.ruitzei.foodie.ui.orderList.OrdersListActivity
import com.ruitzei.foodie.utils.BaseActivity
import com.ruitzei.foodie.utils.viewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        homeViewModel = viewModelProvider()

        auth = FirebaseAuth.getInstance()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (UserData.user?.isDelivery == true) {
            fab.setOnClickListener {
                homeViewModel.openOrdersListAction.sendAction(true)
            }
        } else {
            fab.setOnClickListener {
                startActivity(NewOrderActivity.newIntent(this))
            }
        }

        printFirebaseToken()
    }

    private fun printFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                Log.d(TAG, token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_latest-> {
                startActivity(OrdersListActivity.newIntent(this, showsOld = true))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
