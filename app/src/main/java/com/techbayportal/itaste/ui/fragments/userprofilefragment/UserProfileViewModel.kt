package com.techbayportal.itaste.ui.fragments.userprofilefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.models.SuccessResponse
import com.techbayportal.itaste.data.models.UserPersonalProfileResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch
import java.io.File

class UserProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val dataStoreProvider: DataStoreProvider

) : BaseViewModel() {
    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()
    var onSaveChangesButtonClicked = SingleLiveEvent<Any>()
    val onProfilePicChangeTextClicked = SingleLiveEvent<Any>()
    val onSwitchToPremiumClickedClicked = SingleLiveEvent<Any>()

    val _getUserPersonalProfileResponse = MutableLiveData<Resource<UserPersonalProfileResponse>>()
    val getUserPersonalPersonalResponse: LiveData<Resource<UserPersonalProfileResponse>>
        get() = _getUserPersonalProfileResponse

    val _getUpdateUserPersonalProfileResponse = MutableLiveData<Resource<UserPersonalProfileResponse>>()
    val getUpdateUserPersonalProfileResponse: LiveData<Resource<UserPersonalProfileResponse>>
        get() = _getUpdateUserPersonalProfileResponse

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onSaveChangesButtonClicked(){
        onSaveChangesButtonClicked.call()
    }

    fun onProfilePicChangeTextClicked(){
        onProfilePicChangeTextClicked.call()
    }

    fun onSwitchToPremiumClickedClicked(){
        onSwitchToPremiumClickedClicked.call()
    }


    fun hitGetUserPersonalProfile() {
        viewModelScope.launch {
            _getUserPersonalProfileResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getUserPersonalProfile("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getUserPersonalProfileResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getUserPersonalProfileResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getUserPersonalProfileResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getUserPersonalProfileResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getUserPersonalProfileResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitUpdateUserPersonalProfile(
        first: String, last: String,
        email:String, phone:String, profilePic: File?
    ) {
        viewModelScope.launch {
            _getUpdateUserPersonalProfileResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.updateUserPersonalProfile("Bearer ${loginSession!!.data.access_token}", first,last,email, phone, profilePic).let {
                        if (it.isSuccessful) {
                            _getUpdateUserPersonalProfileResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getUpdateUserPersonalProfileResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getUpdateUserPersonalProfileResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getUpdateUserPersonalProfileResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getUpdateUserPersonalProfileResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }


    fun saveUserObj(loginResponse: LoginResponse){
        viewModelScope.launch {
            dataStoreProvider.saveUserObj(loginResponse)
        }
        LoginSession.getInstance().setLoginResponse(loginResponse)
    }
}