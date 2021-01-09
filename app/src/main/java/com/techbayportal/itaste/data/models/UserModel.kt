package com.techbayportal.itaste.data.models

import okhttp3.MultipartBody

class UserModel {
    var first : String = ""
    var last : String = ""
    var username : String = ""
    var phone : String = ""
    lateinit var profileImage : MultipartBody.Part
    var email : String = ""
    var password : String = ""
    var role : String = ""

}