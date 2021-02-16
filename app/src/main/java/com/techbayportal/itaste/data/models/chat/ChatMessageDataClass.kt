package com.techbayportal.offsidesportsapp.data.models.chat

import java.util.*

class ChatMessageDataClass {
    var createdAt=Date()
    var message=""
    var senderId=""
    var senderName=""
    var id=""
    var seenBy= arrayListOf<String>()
    var deletedFor= arrayListOf<String>()
    var imgStr = ""
    var orderId = ""
    var orderImage = ""
    val messageType = ""
}