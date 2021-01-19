package com.techbayportal.itaste.ui.fragments.deleteaccountfragment

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

class DeleteAccountViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _deleteAccountResponse = MutableLiveData<Resource<SuccessResponse>>()
    val deleteAccountResponse: LiveData<Resource<SuccessResponse>>
        get() = _deleteAccountResponse

    var onConfirmButtonClicked = SingleLiveEvent<Any>()
    var onCancelButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onConfirmButtonClicked(){
        onConfirmButtonClicked.call()
    }

    fun onCancelButtonClicked(){
        onCancelButtonClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun hitDeleteAccount() {
        viewModelScope.launch {
            _deleteAccountResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.deleteAccount("Bearer ${loginSession!!.data.access_token}" ).let {
                        if (it.isSuccessful) {
                            // clearUserObj()
                            _deleteAccountResponse.postValue(Resource.success(it.body()!!))
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _deleteAccountResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _deleteAccountResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _deleteAccountResponse.postValue(Resource.error("" + e.message, null))
                }

            } else _deleteAccountResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}