package com.techbayportal.itaste.data.models

data class ResponseError(
    val message: String,
    val errors: HashMap<String, ArrayList<String>>
)