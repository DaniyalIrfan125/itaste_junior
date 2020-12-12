package com.techbayportal.itaste.ui.fragments.afterlogoutfragment

import com.techbayportal.itaste.utils.SingleLiveEvent

class AfterLogoutViewModel {

    val onLogOutClicked = SingleLiveEvent<Any>()

    fun onLoginClicked() {
        onLogOutClicked.call()
    }
}