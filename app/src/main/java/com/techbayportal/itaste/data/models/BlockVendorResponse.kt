package com.techbayportal.itaste.data.models

data class BlockVendorResponse(
    val message: String,
    val data: BlockVendorData,
    val errors: Any
)

data class BlockVendorData(
    val follow: Boolean
)