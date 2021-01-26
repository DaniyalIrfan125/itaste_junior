package com.techbayportal.itaste.data.models

data class NotificationResponse(
    val message: String,
    val data: ArrayList<NotificationResponseData>,
    val errors: Any
)


data class NotificationResponseData(
    val id: Int,
    val heading: String,
    val image_left: String,
    val image_right: String
)