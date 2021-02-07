package com.techbayportal.itaste.data.models

data class SetFavouriteUnFavouriteResponse(
    val `data`: SetFavouriteUnFavouriteData,
    val message: String
)

data class SetFavouriteUnFavouriteData(
    val is_favourite: Boolean
)