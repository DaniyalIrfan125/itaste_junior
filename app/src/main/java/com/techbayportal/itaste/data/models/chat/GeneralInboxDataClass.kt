package com.techbayportal.itaste.data.models.chat

import com.techbayportal.offsidesportsapp.data.models.chat.UserInfoClass
import java.util.*
import kotlin.collections.ArrayList

class  GeneralInboxDataClass {
    lateinit var lastMsgTime: Date
    lateinit var id: String
    var lastMsg: String? = null
    lateinit var senderId: String
    lateinit var senderName: String
    var users = ArrayList<UserInfoClass>()
    var usersId = ArrayList<String>()
    var isGroupChat: Boolean = false
    var unreadMessages = 0
    var imgStr: String = ""     //todo: implement
    var userOnlineStatus : Boolean = false
}
