package com.techbayportal.itaste.ui.fragments.directmessagesfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent


class DirectMessagesFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    var onNewMessageIconClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSignInButtonClicked = SingleLiveEvent<Any>()

    fun onNewMessageIconClicked(){
        onNewMessageIconClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }
    fun onSignInButtonClicked() {
        onSignInButtonClicked.call()
    }
}