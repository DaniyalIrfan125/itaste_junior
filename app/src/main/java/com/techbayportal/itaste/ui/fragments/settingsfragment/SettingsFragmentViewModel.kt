package com.techbayportal.itaste.ui.fragments.settingsfragment

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

class SettingsFragmentViewModel  @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onContactUsClicked = SingleLiveEvent<Any>()
    val onAboutUsClicked = SingleLiveEvent<Any>()
    val onChangeLanguageClicked = SingleLiveEvent<Any>()
    val onTermAndCondClicked = SingleLiveEvent<Any>()
    val onHelpAndFaqsClicked = SingleLiveEvent<Any>()
    val onAdsClicked = SingleLiveEvent<Any>()
    val onLogoutClicked = SingleLiveEvent<Any>()
    val onSwitchedToPremiumClicked = SingleLiveEvent<Any>()

    private val _logoutResponse = MutableLiveData<Resource<SuccessResponse>>()
    val logoutResponse: LiveData<Resource<SuccessResponse>>
        get() = _logoutResponse

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onContactUsClicked() {
        onContactUsClicked.call()
    }

    fun onAboutUsClicked() {
        onAboutUsClicked.call()
    }

    fun onChangeLanguageClicked() {
        onChangeLanguageClicked.call()
    }
    fun onTermAndCondClicked() {
        onTermAndCondClicked.call()
    }
    fun onHelpAndFaqsClicked() {
        onHelpAndFaqsClicked.call()
    }
    fun onAdsClicked() {
        onAdsClicked.call()
    }
    fun onLogoutClicked() {
        onLogoutClicked.call()
    }
    fun onSwitchedToPremiumClicked() {
        onSwitchedToPremiumClicked.call()
    }

    fun hitLogout() {
        viewModelScope.launch {
            _logoutResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.logout("Bearer ${loginSession!!.data.access_token}" ).let {
                        if (it.isSuccessful) {
                            // clearUserObj()
                            _logoutResponse.postValue(Resource.success(it.body()!!))
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _logoutResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _logoutResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _logoutResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _logoutResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}