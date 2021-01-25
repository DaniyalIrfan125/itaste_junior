package com.techbayportal.itaste.data.models

data class GetAllBlockedUserResponse (
    val message: String,
    val data: ArrayList<GetAllBlockedUserData>,
    val errors: Any
)

data class GetAllBlockedUserData(
    val id: Int,
    val profilePic: String,
    val first_name: String,
    val last_name: String,
    val username: String
)