package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.VerifyOtpResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class ChangePasswordViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val dataStoreProvider: DataStoreProvider
) : BaseViewModel() {

    private val _updatePasswordResponse = MutableLiveData<Resource<VerifyOtpResponse>>()
    val updatePassword: LiveData<Resource<VerifyOtpResponse>>
        get() = _updatePasswordResponse

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSaveButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onBtnClick (){
        onSaveButtonClicked.call()
    }

    fun hitUpdatePassword(phone: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _updatePasswordResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.updatePassword(phone, password, confirmPassword).let {
                        if (it.isSuccessful) {
                           // clearUserObj()
                            _updatePasswordResponse.postValue(Resource.success(it.body()!!))
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _updatePasswordResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _updatePasswordResponse.postValue(Resource.error(extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _updatePasswordResponse.postValue(Resource.error("" + e.message, null))
                }

            } else _updatePasswordResponse.postValue(Resource.error("No internet connection", null))
        }
    }

   /* fun clearUserObj() {
        viewModelScope.launch {
            dataStoreProvider.clearUserObj()
        }
    }*/


}