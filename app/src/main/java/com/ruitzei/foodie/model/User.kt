package com.ruitzei.foodie.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
class User(
    @SerializedName("id") var id: String = "",
    @SerializedName("properties") var userProperties: UserProperties? = null,
    @SerializedName("geometry") var geometry: Address? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "first_name" to userProperties?.name,
            "last_name" to userProperties?.lastName,
            "id" to id
        )
    }

    val name
        get() = userProperties?.name
    val lastName
            get() = userProperties?.lastName
    val phoneNumber
            get() = userProperties?.phoneNumber
    val mail
            get() = userProperties?.mail
    val isDelivery
            get() = userProperties?.isDelivery
    val balance
            get() = userProperties?.balance
    val avatar
            get() = userProperties?.avatar

    val latLong: LatLong
        get() = LatLong(geometry!!.coordinates.first(), geometry!!.coordinates.last())

    companion object {
        fun mockedUser(): User {
            return User(
                userProperties = UserProperties(
                    name ="Jose",
                    lastName = "sarlanga",
                    phoneNumber = "+5491165303000",
                    mail = "sarlagna@gmail.com",
                    isDelivery = false,
                    balance = 1256,
                    avatar = "https://previews.123rf.com/images/fayethequeen/fayethequeen1702/fayethequeen170200011/70740120-cara-del-monstruo-criaturas-de-dibujos-animados-vector-de-ilustraci%C3%B3n-stock-avatar.jpg"
                )
            )
        }
    }
}

@IgnoreExtraProperties
data class UserProperties (
    @PropertyName("first_name") @SerializedName("first_name") var name: String = "",
    @PropertyName("last_name") @SerializedName("last_name") var lastName: String = "",
    @SerializedName("email") var mail: String = "",
    @SerializedName("phone_number") var phoneNumber: String = "",
    @SerializedName("is_delivery") var isDelivery: Boolean = false,
    @SerializedName("balance") var balance: Int = 0,
    @SerializedName("avatar") var avatar: String = ""
)