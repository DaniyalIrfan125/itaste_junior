package com.techbayportal.itaste.data.models

data class VerifyOtpResponse(
    val message: String?,
    val data: VerifyOtpData?,
    val errors: Any?
)

data class VerifyOtpData(
    val access_token : String,
    val id : String,
    val first: String,
    val last: String,
    val username: String,
    val phone: String,
    val email: String,
    val profile_img: String,
    val role: String
)
