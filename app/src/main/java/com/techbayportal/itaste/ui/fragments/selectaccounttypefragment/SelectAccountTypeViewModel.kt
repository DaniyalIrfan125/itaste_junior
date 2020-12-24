package com.techbayportal.itaste.ui.fragments.selectaccounttypefragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class SelectAccountTypeViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onExploreFoodClicked = SingleLiveEvent<Any>()
    val onShowcaseFoodClicked = SingleLiveEvent<Any>()
    val onNextButtonClicked = SingleLiveEvent<Any>()


    fun onExploreFoodClicked() {
        onExploreFoodClicked.call()
    }

    fun onShowcaseFoodClicked() {
        onShowcaseFoodClicked.call()
    }

    fun onNextButtonClicked() {
        onNextButtonClicked.call()
    }
}