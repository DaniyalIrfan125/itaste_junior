package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class SignUpConfigurationsViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onNextButtonClicked = SingleLiveEvent<Any>()
    val onDarkModeButtonClicked = SingleLiveEvent<Any>()
    val onLightModeButtonClicked = SingleLiveEvent<Any>()

    fun onNextButtonClicked() {
        onNextButtonClicked.call()
    }

    fun onDarkModeButtonClicked() {
        onDarkModeButtonClicked.call()
    }

    fun onLightModeButtonClicked() {
        onLightModeButtonClicked.call()
    }

}