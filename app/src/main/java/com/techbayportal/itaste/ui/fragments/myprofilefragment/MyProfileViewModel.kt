package com.techbayportal.itaste.ui.fragments.myprofilefragment

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

class MyProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _logoutResponse = MutableLiveData<Resource<SuccessResponse>>()
    val logoutResponse: LiveData<Resource<SuccessResponse>>
        get() = _logoutResponse



    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onEditProfileClicked = SingleLiveEvent<Any>()
    var onMyCartClicked = SingleLiveEvent<Any>()
    var onChangePasswordClicked = SingleLiveEvent<Any>()
    val onSavedPostsClicked = SingleLiveEvent<Any>()
    val onReportBugClicked = SingleLiveEvent<Any>()
    val onDeleteAccountClicked = SingleLiveEvent<Any>()
    val onBlockedAccountClicked = SingleLiveEvent<Any>()
    val onLogoutClicked = SingleLiveEvent<Any>()


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onEditProfileClicked(){
        onEditProfileClicked.call()
    }

    fun onMyCartClicked(){
        onMyCartClicked.call()
    }

    fun onChangePasswordClicked(){
        onChangePasswordClicked.call()
    }

    fun onSavedPostsClicked(){
        onSavedPostsClicked.call()
    }

    fun onReportBugClicked(){
        onReportBugClicked.call()
    }

    fun onDeleteAccountClicked(){
        onDeleteAccountClicked.call()
    }

    fun onBlockedAccountClicked(){
        onBlockedAccountClicked.call()
    }

    fun onLogoutClicked(){
        onLogoutClicked.call()
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