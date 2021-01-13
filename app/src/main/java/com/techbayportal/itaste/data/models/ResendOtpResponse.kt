package com.techbayportal.itaste.data.models

data class ResendOtpResponse(
    val data: OtpResponseData,
    val message: String


)

data class OtpResponseData(val otp: Int)