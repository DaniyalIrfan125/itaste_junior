package com.techbayportal.itaste.data.models

import java.io.Serializable

class UserPersonalProfileResponse(
    val message: String,
    val data: UserPersonalProfileResponseData,
    val errors: Any
): Serializable

data class UserPersonalProfileResponseData(
    val id: String,
    val profile_pic: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String
)