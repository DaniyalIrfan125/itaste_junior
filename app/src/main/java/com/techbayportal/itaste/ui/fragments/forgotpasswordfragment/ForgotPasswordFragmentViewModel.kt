package com.techbayportal.itaste.ui.fragments.forgotpasswordfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.VerifyOtpResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


class ForgotPasswordFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val _forgotPasswordResponse = MutableLiveData<Resource<VerifyOtpResponse>>()
    val forgotPassword: LiveData<Resource<VerifyOtpResponse>>
        get() = _forgotPasswordResponse

    val onNextButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onNextButtonClicked(){
        onNextButtonClicked.call()
    }


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun forgotPasswordApiCall(phoneNumber: String) {
        viewModelScope.launch {
            _forgotPasswordResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try{
                    mainRepository.forgotPassword(phoneNumber).let {
                        if (it.isSuccessful) {
                            _forgotPasswordResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _forgotPasswordResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _forgotPasswordResponse.postValue(Resource.error(extractErrorMessage(errorMessagesJson), null))
                        }

                    }
                }catch (e:Exception){
                    _forgotPasswordResponse.postValue(Resource.error(""+e.message, null))
                }

            } else _forgotPasswordResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}