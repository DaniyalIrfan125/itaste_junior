package com.techbayportal.itaste.data.models

import java.io.Serializable

class VendorPersonalProfileResponse(
    val message: String,
    val data: VendorPersonalProfileResponseData,
    val errors: Any
): Serializable

data class VendorPersonalProfileResponseData(
    val id: String,
    val profile_pic: String,
    val first_name: String,
    val last_name: String,
    val bio: String,
    val phone: String,
    val email: String
)