package com.techbayportal.itaste.data.models

import java.io.Serializable

data class VendorUpdateProfileResponse(
    val message: String,
    val data: VendorPersonalProfileResponseData,
    val errors: Any
): Serializable

data class VendorUpdateProfileResponseData(
    val first: String,
    val last: String,
    val phone: String,
    val email: String,
    val country_id: String,
    val city_id: String,
    val description: String,
    val profilePic: String,
    val role: String,
    val is_payment_update: Boolean
)