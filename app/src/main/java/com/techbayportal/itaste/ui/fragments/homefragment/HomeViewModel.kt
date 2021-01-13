package com.techbayportal.itaste.ui.fragments.homefragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class HomeViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onHomeConfigButtonClicked = SingleLiveEvent<Any>()
    val tempClicked = SingleLiveEvent<Any>()

    fun onHomeConfigButtonClicked() {
        if(loginSession != null){
            if(loginSession.data.role.equals("user",true)){

            }else{

            }
        }
        onHomeConfigButtonClicked.call()
    }

    fun tempClicked() {
        tempClicked.call()
    }
}