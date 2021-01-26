package com.techbayportal.itaste.ui.fragments.searchfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.GetAllBlockedUserResponse
import com.techbayportal.itaste.data.models.GetAllCategoriesResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val _getAllCategoriesResponse = MutableLiveData<Resource<GetAllCategoriesResponse>>()
    val getAllCategoriesResponse: LiveData<Resource<GetAllCategoriesResponse>>
        get() = _getAllCategoriesResponse




    fun hitGetAllBlockedAccountsApi() {
        viewModelScope.launch {
            _getAllCategoriesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllCategories("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getAllCategoriesResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getAllCategoriesResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getAllCategoriesResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getAllCategoriesResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getAllCategoriesResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }
}