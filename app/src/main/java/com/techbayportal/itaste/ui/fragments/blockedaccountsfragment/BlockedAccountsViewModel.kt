package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.BlockVendorResponse
import com.techbayportal.itaste.data.models.GetAllBlockedUserResponse
import com.techbayportal.itaste.data.models.VendorPersonalProfileResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class BlockedAccountsViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    val _getAllBlockedAccountsResponse = MutableLiveData<Resource<GetAllBlockedUserResponse>>()
    val getAllBlockedAccountsResponse: LiveData<Resource<GetAllBlockedUserResponse>>
        get() = _getAllBlockedAccountsResponse

    val _blockAccountResponse = MutableLiveData<Resource<BlockVendorResponse>>()
    val blockAccountResponse: LiveData<Resource<BlockVendorResponse>>
        get() = _blockAccountResponse

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun hitGetAllBlockedAccountsApi() {
        viewModelScope.launch {
            _getAllBlockedAccountsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllBlockedUsers("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getAllBlockedAccountsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getAllBlockedAccountsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getAllBlockedAccountsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getAllBlockedAccountsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getAllBlockedAccountsResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitBlockAccountApi(vendorId: Int) {
        viewModelScope.launch {
            _blockAccountResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.blockVendor("Bearer ${loginSession!!.data.access_token}", vendorId).let {
                        if (it.isSuccessful) {
                            _blockAccountResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _blockAccountResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _blockAccountResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _blockAccountResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _blockAccountResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

}