package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class ChangePasswordViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSaveButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {

        onBackButtonClicked.call()

    }

    fun onBtnClick (){
        onSaveButtonClicked.call()
    }
}