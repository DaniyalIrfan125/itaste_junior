package com.techbayportal.itaste.data.models

import java.io.Serializable

data class LoginResponse(
    val message: String,
    val data: Data,
    val errors: Any
):Serializable


public data class Data(
    val id : String,
    val first: String,
    val last: String,
    val username: String,
    val phone: String,
    val email: String,
    val profile_img: String,
    val role: String,
    val access_token: String,
    val country_id: String



)