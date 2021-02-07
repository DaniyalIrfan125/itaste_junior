package com.techbayportal.itaste.data.models

import java.io.Serializable

data class VendorPersonalProfileResponse(
    val message: String,
    val data: VendorPersonalProfileResponseData,
    val errors: Any
): Serializable

data class VendorPersonalProfileResponseData(
    val id: Int,

    val first: String,
    val last: String,

    val phone: String,
    val email: String,

    val country_id: String,
    val city_id: String,
    val description: String,
    val profile_pic: String,
    val role: String,
    val is_payment_update: Boolean


)