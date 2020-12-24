package com.techbayportal.itaste.ui.fragments.homefragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class HomeViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onHomeConfigButtonClicked = SingleLiveEvent<Any>()
    val tempClicked = SingleLiveEvent<Any>()

    fun onHomeConfigButtonClicked() {
        onHomeConfigButtonClicked.call()
    }

    fun tempClicked() {
        tempClicked.call()
    }
}