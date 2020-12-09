package com.techbayportal.itaste.ui.fragments.loginfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class LoginViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onLoginClicked = SingleLiveEvent<Void>()
    val onForgotPasswordClicked = SingleLiveEvent<Void>()


    fun onLoginClicked() {
        onLoginClicked.call()
    }

    fun onForgotPasswordClicked() {
        onForgotPasswordClicked.call()
    }

    fun onContinueAsGuestClicked() {

    }
}