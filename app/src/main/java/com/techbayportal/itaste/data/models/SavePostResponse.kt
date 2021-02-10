package com.techbayportal.itaste.data.models

data class SavePostResponse(
    val `data`: SavePostResponseData,
    val message: String
)

data class SavePostResponseData(
    val is_save: Boolean
)