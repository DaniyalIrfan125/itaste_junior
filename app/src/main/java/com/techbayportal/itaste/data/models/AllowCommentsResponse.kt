package com.techbayportal.itaste.data.models

data class AllowCommentsResponse(
    val `data`: AllowCommentsResponseData,
    val message: String
)

data class AllowCommentsResponseData(
    val comments: String
)