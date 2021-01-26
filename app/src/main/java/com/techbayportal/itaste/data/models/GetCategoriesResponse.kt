package com.techbayportal.itaste.data.models

data class GetCategoriesResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
) {

    data class Data(
        val id: Int,
        val name: String
    )

}