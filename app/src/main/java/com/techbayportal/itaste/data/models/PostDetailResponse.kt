package com.techbayportal.itaste.data.models

data class PostDetailResponse(
    val `data`: PostDetailResponseData,
    val message: String
)

data class PostDetailResponseData(
    val post: Post,
    val vendor: Vendor
)

data class Post(
    val allow_comments: String,
    val caption: String,
    val comments: List<Comment>?,
    val cooking_time: String,
    val description : String,
    val id: Int,
    val image: String,
    val is_favourite: Boolean,
    val price: Double,
    val total_likes: Int
)

data class Vendor(
    val first_name: String,
    val last_name: String,
    val location: String,
    val profilePic: String,
    val vendor_id: Int
)

data class Comment(
    val comment: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val profilePic: String,
    val total_likes: Int,
    val user_id: Int
)