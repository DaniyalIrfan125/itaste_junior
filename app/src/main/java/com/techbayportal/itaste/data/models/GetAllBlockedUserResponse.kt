package com.techbayportal.itaste.data.models

data class GetAllBlockedUserResponse (
    val message: String,
    val data: ArrayList<GetAllBlockedUserData>,
    val errors: Any
)

data class GetAllBlockedUserData(
    val id: Int,
    val profilePic: String,
    val first: String,
    val last: String,
    val username: String
)