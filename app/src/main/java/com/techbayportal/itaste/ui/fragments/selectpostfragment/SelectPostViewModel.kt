package com.techbayportal.itaste.ui.fragments.selectpostfragment

import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.utils.SingleLiveEvent

class SelectPostViewModel : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked(){
        onBackButtonClicked.call()
    }
}