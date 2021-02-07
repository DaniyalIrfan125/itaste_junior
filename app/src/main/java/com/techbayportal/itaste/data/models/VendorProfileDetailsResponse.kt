package com.techbayportal.itaste.data.models

data class VendorProfileDetailsResponse(
    val message: String,
    val data: VendorProfileDetailData,
    val errors: Any
)


data class VendorProfileDetailData(
    val id: Int,
    val first: String,
    val last: String,
    val username: String,
    val image: String,
    val bio: String,
    val total_post: Int,
    val total_likes: Int,
    val total_followers: Int,
    val posts: List<PostDetailData>,
    val is_follow: Boolean,
    val is_payment_update: Boolean

)

data class PostDetailData(
    val id: Int,
    val image: String
)