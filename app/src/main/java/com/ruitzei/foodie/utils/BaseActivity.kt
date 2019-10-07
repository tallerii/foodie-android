package com.ruitzei.foodie.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.ruitzei.foodie.model.AbstractPermission

abstract class BaseActivity: AppCompatActivity() {
    var requiredPermissionAction: () -> Unit? = {}
    var PERMISSION_ALL = 721

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            // If one permission was not granted, skip this.
            if (result != 0) {
                finish()
                return
            }
        }

        requiredPermissionAction.invoke()
    }

    fun hasPermissions(context: Context?, abstractPermission: AbstractPermission): Boolean {
        if (context != null) {
            for (permission in abstractPermission.permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }


    fun performPermissionAction(func: () -> Unit, permission: AbstractPermission) {
        requiredPermissionAction = func

        if (hasPermissions(this, permission)) {
            func.invoke()
        } else {
            if (shouldShowRationale(permission)) {
                val viewGroup = (this
                    .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
                Snackbar.make(viewGroup, permission.rationaleMessage,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ok") { ActivityCompat.requestPermissions(this, permission.permissions.toTypedArray(), PERMISSION_ALL) }
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, permission.permissions.toTypedArray(), PERMISSION_ALL)
            }
        }
    }

    private fun shouldShowRationale(permission: AbstractPermission): Boolean {
        permission.permissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, it)) {
                return true
            }
        }


        return false
    }
}