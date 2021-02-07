package com.techbayportal.itaste.ui.fragments.searchfragment

data class SearchModel (
    val twoLayout:TwoLayout,
    val threeLayout:ThreeLayout
) {
    data class TwoLayout (
        val image1:ImageData,
        val image2:ImageData
    )

    data class ThreeLayout (
        val image1:ImageData,
        val image2:ImageData,
        val image3:ImageData
    )

    class ImageData {

    }
}