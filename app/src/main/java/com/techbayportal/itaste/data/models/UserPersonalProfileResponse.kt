package com.techbayportal.itaste.data.models

import java.io.Serializable

class UserPersonalProfileResponse(
    val message: String,
    val data: UserPersonalProfileResponseData,
    val errors: Any
): Serializable

data class UserPersonalProfileResponseData(
    val id: String,
    val first: String,
    val last: String,
    val username: String,
    val phone: String,
    val email: String,
    val profile_pic: String,
    val role: String
)