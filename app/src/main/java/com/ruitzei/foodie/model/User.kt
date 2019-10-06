package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("first_name") var name: String = "",
    @SerializedName("last_name") var lastName: String = "",
    @SerializedName("email") var mail: String = "",
    @SerializedName("phone_number") var phoneNumber: String = "",
    @SerializedName("is_delivery") var isDelivery: Boolean = false,
    @SerializedName("") var balance: Int = 0,
    @SerializedName("avatar") var avatar: String = ""

) {

    companion object {
        fun mockedUser(): User {
            return User(
                name ="Jose",
                lastName = "sarlanga",
                phoneNumber = "+5491165303000",
                mail = "sarlagna@gmail.com",
                isDelivery = false,
                balance = 1256,
                avatar = "https://previews.123rf.com/images/fayethequeen/fayethequeen1702/fayethequeen170200011/70740120-cara-del-monstruo-criaturas-de-dibujos-animados-vector-de-ilustraci%C3%B3n-stock-avatar.jpg"
            )
        }
    }
}