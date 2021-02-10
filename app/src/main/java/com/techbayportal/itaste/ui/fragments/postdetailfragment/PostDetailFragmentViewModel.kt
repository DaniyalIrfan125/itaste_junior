package com.techbayportal.itaste.ui.fragments.postdetailfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

class PostDetailFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    private val _postDetailResponse = MutableLiveData<Resource<PostDetailResponse>>()
    val postDetailResponse: LiveData<Resource<PostDetailResponse>>
        get() = _postDetailResponse

    private val _postCommentResponse = MutableLiveData<Resource<PostCommentResponse>>()
    val postCommentResponse: LiveData<Resource<PostCommentResponse>>
        get() = _postCommentResponse

    private val _favoriteUnfavoriteReponse =
        MutableLiveData<Resource<SetFavouriteUnFavouriteResponse>>()
    val favoriteUnfavoriteReponse: LiveData<Resource<SetFavouriteUnFavouriteResponse>>
        get() = _favoriteUnfavoriteReponse


    private val _allowCommentsResponse =
        MutableLiveData<Resource<AllowCommentsResponse>>()
    val allowCommentsResponse: LiveData<Resource<AllowCommentsResponse>>
        get() = _allowCommentsResponse

    private val _favoriteUnfavoriteCommentReponse =
        MutableLiveData<Resource<CommentFavouriteResponse>>()
    val favoriteUnfavoriteCommentReponse: LiveData<Resource<CommentFavouriteResponse>>
        get() = _favoriteUnfavoriteCommentReponse


    private val _postDeleteReponse = MutableLiveData<Resource<PostDetailResponse>>()
    val postDeleteResponse: LiveData<Resource<PostDetailResponse>>
        get() = _postDeleteReponse

    private val _savePostResponse =
        MutableLiveData<Resource<SavePostResponse>>()
    val savePostResponse: LiveData<Resource<SavePostResponse>>
        get() = _savePostResponse


    var onVendorProfileHeaderClicked = SingleLiveEvent<Any>()
    var onSendButtonClicked = SingleLiveEvent<Any>()
    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onEditPostButtonClicked = SingleLiveEvent<Any>()
    val onMoreButtonClicked = SingleLiveEvent<Any>()
    val onSaveButtonClicked = SingleLiveEvent<Any>()

    fun onVendorProfileHeaderClicked() {
        onVendorProfileHeaderClicked.call()
    }

    fun onSendButtonClicked() {
        onSendButtonClicked.call()
    }

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onEditPostButtonClicked() {
        onEditPostButtonClicked.call()
    }

    fun onMoreButtonClicked() {
        onMoreButtonClicked.call()
    }

    fun onSavePostButtonClicked() {
        onSaveButtonClicked.call()
    }


    fun postDetailCall(
        postId: Int
    ) {
        viewModelScope.launch {
            _postDetailResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getPostDetail(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId
                    ).let {
                        if (it.isSuccessful) {
                            _postDetailResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _postDetailResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _postDetailResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _postDetailResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _postDetailResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun postCommentCall(
        postId: Int,
        comment: String
    ) {
        viewModelScope.launch {
            _postCommentResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.postComment(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId,
                        comment
                    ).let {
                        if (it.isSuccessful) {
                            _postCommentResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _postCommentResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _postCommentResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _postCommentResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _postCommentResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun favouriteUnFavouriteCall(
        postId: Int
    ) {
        viewModelScope.launch {
            _favoriteUnfavoriteReponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.favouriteUnFavoritePost(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId
                    ).let {
                        if (it.isSuccessful) {
                            _favoriteUnfavoriteReponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _favoriteUnfavoriteReponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _favoriteUnfavoriteReponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _favoriteUnfavoriteReponse.postValue(Resource.error("" + e.message, null))
                }
            } else _favoriteUnfavoriteReponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }

    fun favouriteCommentCall(
        commentId: Int
    ) {
        viewModelScope.launch {
            _favoriteUnfavoriteCommentReponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.favouriteUnFavoriteComment(
                        "Bearer ${loginSession!!.data.access_token}",
                        commentId
                    ).let {
                        if (it.isSuccessful) {
                            _favoriteUnfavoriteCommentReponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _favoriteUnfavoriteCommentReponse.postValue(
                                Resource.error(
                                    it.message(),
                                    null
                                )
                            )
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _favoriteUnfavoriteCommentReponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _favoriteUnfavoriteCommentReponse.postValue(
                        Resource.error(
                            "" + e.message,
                            null
                        )
                    )
                }
            } else _favoriteUnfavoriteCommentReponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
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


    fun deletePost(
        postId: Int
    ) {
        viewModelScope.launch {
            _postDeleteReponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.deletePost(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId
                    ).let {
                        if (it.isSuccessful) {
                            _postDeleteReponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _postDeleteReponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _postDeleteReponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _postDeleteReponse.postValue(Resource.error("" + e.message, null))
                }
            } else _postDeleteReponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun allowComments(
        postId: Int,
        allowComments: Int
    ) {
        viewModelScope.launch {
            _allowCommentsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.allowComments(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId,
                        allowComments
                    ).let {
                        if (it.isSuccessful) {
                            _allowCommentsResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _allowCommentsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _allowCommentsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _allowCommentsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _allowCommentsResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    fun savePost(
        postId: Int
    ) {
        viewModelScope.launch {
            _savePostResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.savePost(
                        "Bearer ${loginSession!!.data.access_token}",
                        postId
                    ).let {
                        if (it.isSuccessful) {
                            _savePostResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _savePostResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _savePostResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _savePostResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _savePostResponse.postValue(Resource.error("No internet connection", null))
        }
    }

}