package com.techbayportal.itaste.ui.fragments.changeexistingpasswordfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class ChangeExistingPasswordViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBtnClicked = SingleLiveEvent<Any>()


    fun onBtnClicked(){
        onBtnClicked.call()
    }
}