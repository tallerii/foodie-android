package com.ruitzei.foodie.model

import com.google.firebase.database.PropertyName

class ChatMessage (
    @PropertyName("message") var message: String = "",
    @PropertyName("senderId") var senderId: String = "",
    @PropertyName("senderName") var senderName: String = ""
)