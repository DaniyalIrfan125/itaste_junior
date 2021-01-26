package com.techbayportal.itaste.data.models

data class GetTimeSuggestionResponse(
    val `data`: List<GetTimeSuggestionData>,
    val message: String
)

data class GetTimeSuggestionData(
    val time: String
)