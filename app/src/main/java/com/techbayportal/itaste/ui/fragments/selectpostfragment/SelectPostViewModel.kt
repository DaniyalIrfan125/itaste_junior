package com.techbayportal.itaste.ui.fragments.selectpostfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.GetCategoriesResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class SelectPostViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onNextBtnClicked = SingleLiveEvent<Any>()
    val onCameraBtnClicked = SingleLiveEvent<Any>()

    fun onNextBtnClicked() {
        onNextBtnClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onCameraBtnClicked() {
        onCameraBtnClicked.call()
    }

    private val _getCategoriesResponse = MutableLiveData<Resource<GetCategoriesResponse>>()
    val getCategoriesResponse: LiveData<Resource<GetCategoriesResponse>>
        get() = _getCategoriesResponse

    fun getCategories() {
        viewModelScope.launch {
            _getCategoriesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCategories("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getCategoriesResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _getCategoriesResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getCategoriesResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCategoriesResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getCategoriesResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}