package com.ruitzei.foodie.model

abstract class AbstractPermission {
    abstract val permissions: List<String>
    abstract val rationaleMessage: Int
}