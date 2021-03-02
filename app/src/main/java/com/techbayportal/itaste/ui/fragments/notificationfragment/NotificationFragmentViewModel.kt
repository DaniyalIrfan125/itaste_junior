package com.techbayportal.itaste.ui.fragments.notificationfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.NotificationResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class NotificationFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    val dataStoreProvider: DataStoreProvider
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val _getNotificationsResponse = MutableLiveData<Resource<NotificationResponse>>()
    val getNotificationsResponse: LiveData<Resource<NotificationResponse>>
        get() = _getNotificationsResponse

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSignInButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onSignInButtonClicked() {
        onSignInButtonClicked.call()
    }

    fun hitGetNotificationApi() {
        viewModelScope.launch {
            _getNotificationsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getNotifications("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getNotificationsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getNotificationsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getNotificationsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getNotificationsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getNotificationsResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun setGuestMode(isGuestMode :Boolean){
        viewModelScope.launch {
            dataStoreProvider.guestMode(isGuestMode)
        }
    }
}