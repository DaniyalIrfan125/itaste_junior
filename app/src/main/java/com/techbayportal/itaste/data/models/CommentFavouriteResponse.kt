package com.techbayportal.itaste.data.models

data class CommentFavouriteResponse(
    val `data`: Data,
    val message: String
)

data class CommentFavouriteData(
    val is_like: Boolean
)