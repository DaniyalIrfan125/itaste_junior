package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class OtpVerificationViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onNextButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()


    fun onNextButtonClicked() {
        onNextButtonClicked.call()
    }


    fun onBackButtonClicked() {

        onBackButtonClicked.call()

    }
}