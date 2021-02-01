package com.techbayportal.itaste.data.models

data class GetAllCitiesResponse(
    val message: String,
    val data: ArrayList<GetAllCitiesData>,
    val errors: Any
)

data class GetAllCitiesData(
    val id: Int,
    val name: String,
    val select: Boolean
)