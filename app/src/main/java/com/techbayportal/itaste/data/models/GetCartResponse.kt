package com.techbayportal.itaste.data.models

data class GetCartResponse(
    val `data`: GetCartData,
    val message: String
)

data class GetCartData(
    val cart: List<Cart>
)

data class Cart(
    val post: CartPost,
    val vendor: CartVendor
)

data class CartPost(
    val caption: String,
    val description: String,
    val id: Int,
    val image: String,
    val quantity: Int,
    val order_no: String
)

data class CartVendor(
    val address: String,
    val first: String,
    val id: Int,
    val last: String,
    val profile_pic: String


)