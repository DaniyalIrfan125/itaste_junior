package com.techbayportal.itaste.data.models

data class GetAllSavedPost(
    val `data`: List<GetAllSavedData>,
    val message: String
)

data class GetAllSavedData(
    val id: Int,
    val image: String
)