package com.techbayportal.itaste.data.models

data class PackagesResponse(
    val message: String,
    val data: ArrayList<PackagesResponseData>,
    val errors: Any
)


data class PackagesResponseData(
    val name: String,
    val price: Int,
    val post_per_week: Int,
    val weekly_update: String,
    val cancelation_time: String,
    val post_week: String,
    val change_subscription: String
)