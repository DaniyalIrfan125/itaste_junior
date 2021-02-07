package com.techbayportal.itaste.ui.fragments.postfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch
import java.io.File

class PostViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _addPostsResponse = MutableLiveData<Resource<AddPostResponse>>()
    val addPostsResponse: LiveData<Resource<AddPostResponse>>
        get() = _addPostsResponse

    private val _updatePostsResponse = MutableLiveData<Resource<UpdatePostResponse>>()
    val updatePostsResponse: LiveData<Resource<UpdatePostResponse>>
        get() = _updatePostsResponse



    private val _getTimeSuggestionResponse = MutableLiveData<Resource<GetTimeSuggestionResponse>>()
    val getTimeSuggestionResponse: LiveData<Resource<GetTimeSuggestionResponse>>
        get() = _getTimeSuggestionResponse

    private val _editPostResponse = MutableLiveData<Resource<EditPostResponse>>()
    val editPostResponse: LiveData<Resource<EditPostResponse>>
        get() = _editPostResponse

    val onPostBtnClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()

    fun onPostBtnClicked() {
        onPostBtnClicked.call()
    }


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }


    fun addPostCall(
        categoryId: Int,
        image: File,
        caption: String,
        price: Double,
        cookingTime: String,
        description: String,
        allowComments: Int
    ) {
        viewModelScope.launch {
            _addPostsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.addPost(
                        categoryId,
                        image,
                        caption,
                        price,
                        cookingTime,
                        description,
                        allowComments,
                        "Bearer ${loginSession!!.data.access_token}"
                    ).let {
                        if (it.isSuccessful) {
                            _addPostsResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _addPostsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _addPostsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _addPostsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _addPostsResponse.postValue(Resource.error("No internet connection", null))
        }
    }




    fun getTimeSuggestions() {
        viewModelScope.launch {
            _getTimeSuggestionResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getTimeSuggestion("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getTimeSuggestionResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _getTimeSuggestionResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getTimeSuggestionResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getTimeSuggestionResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getTimeSuggestionResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun updatePostCall(
        postId: Int,
        categoryId: Int,
        caption: String,
        price: Double,
        cookingTime: String,
        description: String,
        allowComments: Int
    ) {
        viewModelScope.launch {
            _updatePostsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.updatePost(
                        postId,
                        categoryId,
                        caption,
                        price,
                        cookingTime,
                        description,
                        allowComments,
                        "Bearer ${loginSession!!.data.access_token}"
                    ).let {
                        if (it.isSuccessful) {
                            _updatePostsResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _updatePostsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _updatePostsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _updatePostsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _updatePostsResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun editPostCall(
        postId: Int
    ) {
        viewModelScope.launch {
            _editPostResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getEditPost(
                        "Bearer ${loginSession!!.data.access_token}",postId
                    ).let {
                        if (it.isSuccessful) {
                            _editPostResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _editPostResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _editPostResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _editPostResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _editPostResponse.postValue(Resource.error("No internet connection", null))
        }
    }
}