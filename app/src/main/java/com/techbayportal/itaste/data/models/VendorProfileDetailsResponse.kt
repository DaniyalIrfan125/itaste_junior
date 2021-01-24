package com.techbayportal.itaste.data.models

data class VendorProfileDetailsResponse(
    val message: String,
    val data: VendorProfileDetailData,
    val errors: Any
)


data class VendorProfileDetailData(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val username: String,
    val image: String,
    val bio: String,
    val total_post: Int,
    val total_likes: Int,
    val total_followers: Int,
    val posts: List<PostDetailData>
)

data class PostDetailData(
    val id: Int,
    val image: String
)