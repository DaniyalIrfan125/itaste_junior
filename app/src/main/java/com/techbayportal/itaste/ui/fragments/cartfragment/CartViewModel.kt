package com.techbayportal.itaste.ui.fragments.cartfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.GetCartResponse
import com.techbayportal.itaste.data.models.RemoveCartResponse
import com.techbayportal.itaste.data.models.RemoveSavedPost
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class CartViewModel @ViewModelInject constructor(private val mainRepository: MainRepository,
                                                 private val networkHelper: NetworkHelper
): BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }


    private val _getCartResponse =
        MutableLiveData<Resource<GetCartResponse>>()
    val getCartResponse: LiveData<Resource<GetCartResponse>>
        get() = _getCartResponse

    fun getCart(
    ) {
        viewModelScope.launch {
            _getCartResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getCart(
                        "Bearer ${loginSession!!.data.access_token}"
                    ).let {
                        if (it.isSuccessful) {
                            _getCartResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _getCartResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getCartResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getCartResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getCartResponse.postValue(Resource.error("No internet connection", null))
        }
    }



    private val _quantityResponse =
        MutableLiveData<Resource<GetCartResponse>>()
    val quantityResponse: LiveData<Resource<GetCartResponse>>
        get() = _quantityResponse

    fun addRemoveCartQuantity(
        postId: Int,
        quantity : Int
    ) {
        viewModelScope.launch {
            _quantityResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.addRemoveCart(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId,
                        quantity

                    ).let {
                        if (it.isSuccessful) {
                            _quantityResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _quantityResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _quantityResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _quantityResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _quantityResponse.postValue(Resource.error("No internet connection", null))
        }
    }



    private val _removeItemCartResponse =
        MutableLiveData<Resource<RemoveCartResponse>>()
    val removeItemCartResponse: LiveData<Resource<RemoveCartResponse>>
        get() = _removeItemCartResponse

    fun removeCartItem(
        postId: Int
    ) {
        viewModelScope.launch {
            _removeItemCartResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.removeItemCart(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId

                    ).let {
                        if (it.isSuccessful) {
                            _removeItemCartResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _removeItemCartResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _removeItemCartResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _removeItemCartResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _removeItemCartResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}