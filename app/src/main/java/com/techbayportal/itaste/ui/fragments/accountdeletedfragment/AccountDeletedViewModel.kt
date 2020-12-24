package com.techbayportal.itaste.ui.fragments.accountdeletedfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class AccountDeletedViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    var onDoneAndExitButtonClicked = SingleLiveEvent<Any>()
    var onCreateNewAccountButtonClicked = SingleLiveEvent<Any>()
    val onBackToLoginButtonClicked = SingleLiveEvent<Any>()

    fun onDoneAndExitButtonClicked(){
        onDoneAndExitButtonClicked.call()
    }

    fun onCreateNewAccountButtonClicked(){
        onCreateNewAccountButtonClicked.call()
    }

    fun onBackToLoginButtonClicked() {
        onBackToLoginButtonClicked.call()
    }
}