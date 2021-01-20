package com.techbayportal.itaste.ui.fragments.profilefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.SuccessResponse
import com.techbayportal.itaste.data.models.UserPersonalProfileResponse
import com.techbayportal.itaste.data.models.VendorProfileDetailData
import com.techbayportal.itaste.data.models.VendorProfileDetailsResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onFollowButtonClicked = SingleLiveEvent<Any>()
    val onDirectMessageButtonClicked = SingleLiveEvent<Any>()

    val _getVendorProfileDetailResponse = MutableLiveData<Resource<VendorProfileDetailsResponse>>()
    val getVendorProfileDetailResponse: LiveData<Resource<VendorProfileDetailsResponse>>
        get() = _getVendorProfileDetailResponse

    val _getReportBugResponse = MutableLiveData<Resource<SuccessResponse>>()
    val getReportBugResponse: LiveData<Resource<SuccessResponse>>
        get() = _getReportBugResponse

    val _getSetFollowResponse = MutableLiveData<Resource<SuccessResponse>>()
    val getSetFollowResponse: LiveData<Resource<SuccessResponse>>
        get() = _getSetFollowResponse

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onFollowButtonClicked() {
        onFollowButtonClicked.call()
    }

    fun onDirectMessageButtonClicked() {
        onDirectMessageButtonClicked.call()
    }


    fun hitGetVendorProfileDetails(vendorId : Int) {
        viewModelScope.launch {
            _getVendorProfileDetailResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getVendorProfileDetails("Bearer ${loginSession!!.data.access_token}", vendorId).let {
                        if (it.isSuccessful) {
                            _getVendorProfileDetailResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getVendorProfileDetailResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getVendorProfileDetailResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getVendorProfileDetailResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getVendorProfileDetailResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitReportBugApi(message : String) {
        viewModelScope.launch {
            _getReportBugResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.reportBug("Bearer ${loginSession!!.data.access_token}", message).let {
                        if (it.isSuccessful) {
                            _getReportBugResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getReportBugResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getReportBugResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getReportBugResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getReportBugResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitSetFollowApi(vendorId : Int) {
        viewModelScope.launch {
            _getSetFollowResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.setFollow("Bearer ${loginSession!!.data.access_token}", vendorId).let {
                        if (it.isSuccessful) {
                            _getSetFollowResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getSetFollowResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getSetFollowResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getSetFollowResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getSetFollowResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }
}