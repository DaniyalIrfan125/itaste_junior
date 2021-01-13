package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.models.ResendOtpResponse
import com.techbayportal.itaste.data.models.VerifyOtpResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class OtpVerificationViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val dataStoreProvider: DataStoreProvider

) : BaseViewModel() {

    //val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _verifyOtpResponse = MutableLiveData<Resource<LoginResponse>>()
    val verifyOtpResponse: LiveData<Resource<LoginResponse>>
        get() = _verifyOtpResponse

    private val _resentVerifyOtpResponse = MutableLiveData<Resource<ResendOtpResponse>>()
    val resentVerifyOtpResponse: LiveData<Resource<ResendOtpResponse>>
        get() = _resentVerifyOtpResponse

    val onNextButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onNextButtonClicked() {
        onNextButtonClicked.call()
    }


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun verifyOTP(code: Int, phone: String, type:String) {
        viewModelScope.launch {
            _verifyOtpResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.verifyOtp(code, phone, type).let {
                        if (it.isSuccessful) {
                            _verifyOtpResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _verifyOtpResponse.postValue(Resource.error(it.message(), null))
                        } else  {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _verifyOtpResponse.postValue(Resource.error(extractErrorMessage(errorMessagesJson), null))
                        }

                    }
                } catch (e: Exception) {
                    _verifyOtpResponse.postValue(Resource.error("" + e.message, null))
                }

            } else _verifyOtpResponse.postValue(Resource.error("No internet connection", null))
        }
    }



    fun hitResentOtp(phone: String, type: String) {
        viewModelScope.launch {
            _resentVerifyOtpResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.resentOtp(phone, type).let {
                        if (it.isSuccessful) {
                            _resentVerifyOtpResponse.postValue(Resource.success(it.body()!!))
//                            saveUserObj(it.body()!!)
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _resentVerifyOtpResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _resentVerifyOtpResponse.postValue(Resource.error(extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _resentVerifyOtpResponse.postValue(Resource.error(""+e.message, null))
                }

            } else _resentVerifyOtpResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }

    fun saveUserObj(loginResponse: LoginResponse){
        viewModelScope.launch {
            dataStoreProvider.saveUserObj(loginResponse)
        }
        LoginSession.getInstance().setLoginResponse(loginResponse)
    }


}