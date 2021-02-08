package com.techbayportal.itaste.ui.fragments.choosepackagefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.NotificationResponse
import com.techbayportal.itaste.data.models.PackagesResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class ChoosePackageViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onProceedButtonClicked = SingleLiveEvent<Any>()
    val onCancelButtonClicked = SingleLiveEvent<Any>()


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onProceedButtonClicked() {
        onProceedButtonClicked.call()
    }

    fun onCancelButtonClicked() {
        onCancelButtonClicked.call()
    }


    val _getPackagesResponse = MutableLiveData<Resource<PackagesResponse>>()
    val getPackagesResponse: LiveData<Resource<PackagesResponse>>
        get() = _getPackagesResponse

    fun hitGetPackagesApi() {
        viewModelScope.launch {
            _getPackagesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getPackages().let {
                        if (it.isSuccessful) {
                            _getPackagesResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getPackagesResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getPackagesResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getPackagesResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getPackagesResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }
}