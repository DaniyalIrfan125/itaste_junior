package com.techbayportal.itaste.ui.fragments.selectpostfragment

import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.utils.SingleLiveEvent

class SelectPostViewModel : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onNextBtnClicked = SingleLiveEvent<Any>()
    val onCameraBtnClicked = SingleLiveEvent<Any>()

    fun onNextBtnClicked() {
        onNextBtnClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onCameraBtnClicked() {
        onCameraBtnClicked.call()
    }
}