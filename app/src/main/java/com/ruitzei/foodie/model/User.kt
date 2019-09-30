package com.ruitzei.foodie.model

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("") var name: String = "",
    @SerializedName("") var phoneNumber: String = "",
    @SerializedName("") var mail: String = "",
    @SerializedName("") var rol: String = "",
    @SerializedName("") var subscription: String = "",
    @SerializedName("") var balance: Int = 0,
    @SerializedName("") var avatar: String = ""

) {

    companion object {
        fun mockedUser(): User {
            return User(
                name ="Jose Sarlanga",
                phoneNumber = "+5491165303000",
                mail = "sarlagna@gmail.com",
                rol = "Rol",
                subscription =  "This is the subscription",
                balance = 1256,
                avatar = "https://previews.123rf.com/images/fayethequeen/fayethequeen1702/fayethequeen170200011/70740120-cara-del-monstruo-criaturas-de-dibujos-animados-vector-de-ilustraci%C3%B3n-stock-avatar.jpg"
            )
        }
    }
}