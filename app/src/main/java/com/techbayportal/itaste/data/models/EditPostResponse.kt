package com.techbayportal.itaste.data.models

data class EditPostResponse(
    val `data`: EditPostResponseData,
    val message: String
)

data class EditPostResponseData(
    val allow_comments: String,
    val caption: String,
    val category_id: String,
    val cooking_time: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: String
)