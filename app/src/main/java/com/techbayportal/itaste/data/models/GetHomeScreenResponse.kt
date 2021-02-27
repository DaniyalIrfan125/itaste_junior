package com.techbayportal.itaste.data.models

class GetHomeScreenResponse (
    val message: String,
    val promotion: PromotionData,
    val data: ArrayList<GetHomeScreenData>,
    val errors: Any
)

public data class PromotionData(
    val id : Int,
    val text: String,
    val offer_end: String,
    val banner: String
)

data class GetHomeScreenData(
    val id: Int,
    val profilePic: String,
    val first: String,
    val last: String,
    val location: String,
    val post: ArrayList<GetHomeScreenPostsData>
)

data class GetHomeScreenPostsData(
    val vendor_id : Int,
    val id: Int,
    val image: String
)