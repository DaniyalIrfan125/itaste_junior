package com.techbayportal.itaste.ui.fragments.foloosipaymentfragment

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
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class FoloosiPaymentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val _getCheckoutResponse = MutableLiveData<Resource<SuccessResponse>>()
    val getCheckoutResponse: LiveData<Resource<SuccessResponse>>
        get() = _getCheckoutResponse


    fun hitCheckOutApi(transaction_id: Int, package_id : Int, amount : Int ) {
        viewModelScope.launch {
            _getCheckoutResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.checkOutApi("Bearer ${loginSession!!.data.access_token}", transaction_id, package_id, amount).let {
                        if (it.isSuccessful) {
                            _getCheckoutResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getCheckoutResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getCheckoutResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCheckoutResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getCheckoutResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }



}