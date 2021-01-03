package com.techbayportal.itaste.data.models

import java.io.Serializable


data class LoginResponse(
    val message: String,
    val data: Data,
    val errors: Any
):Serializable


public data class Data(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val tokenResult: String,
    val token_type: String
)