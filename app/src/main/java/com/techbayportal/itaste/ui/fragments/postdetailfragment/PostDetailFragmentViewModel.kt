package com.techbayportal.itaste.ui.fragments.postdetailfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
class PostDetailFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    var onVendorProfileHeaderClicked = SingleLiveEvent<Any>()
    var onSendButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onEditPostButtonClicked = SingleLiveEvent<Any>()
    val onMoreButtonClicked = SingleLiveEvent<Any>()

    fun onVendorProfileHeaderClicked(){
        onVendorProfileHeaderClicked.call()
    }

    fun onSendButtonClicked(){
        onSendButtonClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onEditPostButtonClicked(){
        onEditPostButtonClicked.call()
    }

    fun onMoreButtonClicked(){
        onMoreButtonClicked.call()
    }

}