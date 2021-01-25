package com.techbayportal.itaste.data.models


data class FollowResponse(
    val message: String,
    val data: FollowData,
    val errors: Any
)

data class FollowData(
    val follow: Boolean
)