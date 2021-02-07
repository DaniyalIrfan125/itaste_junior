package com.techbayportal.itaste.data.models

class SearchAndFilterResponse(
    val message: String,
    val data: ArrayList<SearchAndFilterResponseData>,
    val errors: Any
)

data class SearchAndFilterResponseData(
    val id: Int,
    val image: String
)