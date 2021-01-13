package com.techbayportal.itaste.data.models

data class GetAllCountriesResponse(
    val message: String,
    val data: ArrayList<GetAllCountriesData>,
    val errors: Any
)

data class GetAllCountriesData(
    val id: String,
    val name: String,
    val flag: String
    )

