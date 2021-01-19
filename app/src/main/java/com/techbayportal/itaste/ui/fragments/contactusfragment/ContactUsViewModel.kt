package com.techbayportal.itaste.ui.fragments.contactusfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.SuccessResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class ContactUsViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _contactUsResponse = MutableLiveData<Resource<SuccessResponse>>()
    val contactUsResponse: LiveData<Resource<SuccessResponse>>
        get() = _contactUsResponse

    val loginSession = LoginSession.getInstance().getLoginResponse()

    var onConfirmButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onConfirmButtonClicked(){
        onConfirmButtonClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun hitContactUs(name : String, email: String, message: String) {
        viewModelScope.launch {
            _contactUsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.contactUs("Bearer ${loginSession!!.data.access_token}",name,email,message).let {
                        if (it.isSuccessful) {
                            // clearUserObj()
                            _contactUsResponse.postValue(Resource.success(it.body()!!))
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _contactUsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _contactUsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _contactUsResponse.postValue(Resource.error("" + e.message, null))
                }

            } else _contactUsResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}