package com.techbayportal.offsidesportsapp.data.models.chat

import java.util.*

class GroupChatDataClass {
//    var groupId :String?=null
    var id :String?=null
    var message :String?=null
    var senderName :String?=null
    var senderId :String?=null
    var createdAt =Date()
    var seenBy= arrayListOf<String>()
    var deletedFor= arrayListOf<String>()
    var imgStr :String = ""

}