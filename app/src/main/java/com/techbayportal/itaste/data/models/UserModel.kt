package com.techbayportal.itaste.data.models

import okhttp3.MultipartBody
import java.io.File
import java.lang.reflect.Array

class UserModel {
    var id : Int = 0
    var first : String = ""
    var last : String = ""
    var username : String = ""
    var phone : String = ""
    lateinit var profileImage : File
    var email : String = ""
    var password : String = ""
    var role : String = ""

    //vendor fields

    var country_id : Int = 0
    var city_id : Int = 0
   // var days_of_week : ArrayList<String> = ArrayList()
   // var is_deliverable : Int? = 0
    var password_confirmation : String = ""
    var description : String = ""


}