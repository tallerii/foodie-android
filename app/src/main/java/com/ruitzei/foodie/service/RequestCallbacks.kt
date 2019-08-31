package com.ruitzei.foodie.service

/**
 * Created by ruitzei on 3/17/18.
 */
interface RequestCallbacks<in T> {
    fun onSuccess(response: T)
    fun onFailure(error: String?, code: Int, t: Throwable)
}