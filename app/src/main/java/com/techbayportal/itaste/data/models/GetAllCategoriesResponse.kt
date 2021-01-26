package com.techbayportal.itaste.data.models

data class GetAllCategoriesResponse(
    val message: String,
    val data: ArrayList<GetAllCategoriesData>,
    val errors: Any
)

data class GetAllCategoriesData(
    val id: Int,
    val name: String
)