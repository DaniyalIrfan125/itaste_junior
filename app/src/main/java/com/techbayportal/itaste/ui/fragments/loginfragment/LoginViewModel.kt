package com.techbayportal.itaste.ui.fragments.loginfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {



    val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    private val _saveUserName = MutableLiveData<String>()
    val saveUserName: LiveData<String>
        get() = _saveUserName

    val onLoginClicked = SingleLiveEvent<Any>()
    val onForgotPasswordClicked = SingleLiveEvent<Any>()
    val onSignUpClicked = SingleLiveEvent<Any>()

    fun onLoginClicked() {
        onLoginClicked.call()
    }

    fun onForgotPasswordClicked() {
        onForgotPasswordClicked.call()
    }

    fun onSignUpClicked() {
        onSignUpClicked.call()
    }

    fun onContinueAsGuestClicked() {

    }

    fun loginApiCall(emailPhone: String, password: String) {
        viewModelScope.launch {
            _loginResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.login(emailPhone, password).let {
                        if (it.isSuccessful) {
                            _loginResponse.postValue(Resource.success(it.body()!!))
                            LoginSession.getInstance().setLoginResponse(it.body())
                           // dataStoreProvider.storeUserData(it.body()!!)
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _loginResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _loginResponse.postValue(Resource.error(extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _loginResponse.postValue(Resource.error(""+e.message, null))
                }
            } else _loginResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}