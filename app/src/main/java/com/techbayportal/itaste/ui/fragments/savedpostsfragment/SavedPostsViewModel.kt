package com.techbayportal.itaste.ui.fragments.savedpostsfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.CommentFavouriteResponse
import com.techbayportal.itaste.data.models.GetAllSavedPost
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class SavedPostsViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()


    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    private val _getSavedPostResponse =
        MutableLiveData<Resource<GetAllSavedPost>>()
    val getSavedPostResponse: LiveData<Resource<GetAllSavedPost>>
        get() = _getSavedPostResponse


    fun allSavedPost(
    ) {
        viewModelScope.launch {
            _getSavedPostResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllSavedPost(
                        "Bearer ${loginSession!!.data.access_token}"
                    ).let {
                        if (it.isSuccessful) {
                            _getSavedPostResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _getSavedPostResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getSavedPostResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getSavedPostResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getSavedPostResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}