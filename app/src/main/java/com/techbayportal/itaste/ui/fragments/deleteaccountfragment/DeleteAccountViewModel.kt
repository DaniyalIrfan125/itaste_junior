package com.techbayportal.itaste.ui.fragments.deleteaccountfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class DeleteAccountViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    var onConfirmButtonClicked = SingleLiveEvent<Any>()
    var onCancelButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onConfirmButtonClicked(){
        onConfirmButtonClicked.call()
    }

    fun onCancelButtonClicked(){
        onCancelButtonClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }
}