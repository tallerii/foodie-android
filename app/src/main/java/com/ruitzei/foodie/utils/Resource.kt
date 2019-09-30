package com.ruitzei.foodie.utils

import androidx.annotation.Nullable
import com.ruitzei.foodie.service.ServerError

class Resource<T> private constructor(val status: Status, val data: T?, val errors: List<ServerError>? = null) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(status = Status.SUCCESS, data = data)
        }

        fun <T> error(errors: List<ServerError>, @Nullable data: T?): Resource<T> {
            return Resource(Status.ERROR, data, errors)
        }

        fun <T> loading(@Nullable data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data)
        }
    }
}