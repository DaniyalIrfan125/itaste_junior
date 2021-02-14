package com.techbayportal.itaste.ui.fragments.savedpostsfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.CommentFavouriteResponse
import com.techbayportal.itaste.data.models.GetAllSavedPost
import com.techbayportal.itaste.data.models.RemoveSavedPost
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
    val onRemoveButtonClicked = SingleLiveEvent<Any>()
    val onCancelButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onCancelButtonClicked() {
        onCancelButtonClicked.call()
    }

    fun onRemoveButtonClicked() {
        onRemoveButtonClicked.call()
    }

    private val _getSavedPostResponse =
        MutableLiveData<Resource<GetAllSavedPost>>()
    val getSavedPostResponse: LiveData<Resource<GetAllSavedPost>>
        get() = _getSavedPostResponse


    private val _removeSavedPostResponse =
        MutableLiveData<Resource<RemoveSavedPost>>()
    val removeSavedPostResponse: LiveData<Resource<RemoveSavedPost>>
        get() = _removeSavedPostResponse

    fun allSavedPost(
    ) {
        viewModelScope.launch {
            _getSavedPostResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllSavedPost(
                        "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNDZlNmM4OGI2MDFjY2YwYWJkMDQ4ZmQwYTg1NDNmY2ZiY2FiOTlkNjdjYjQ2MzhkODAwODVkYTZmMGEwMjEyMjQ2YjUxOTg4NmZlY2JlMzgiLCJpYXQiOjE2MTI5NTA0NDUsIm5iZiI6MTYxMjk1MDQ0NSwiZXhwIjoxNjQ0NDg2NDQ1LCJzdWIiOiIxNzciLCJzY29wZXMiOltdfQ.BMqYqfg9zV62zaNRf49KHGq2Va7h9A5XQ9qSm4P0SAK-CFAfM9MD4htDoBdN05U8qQLoVD5g1pDPLLAwtxxndwMgTVP9IvoqurXlnCldGJj_zIyH0zhEGhpwbNa1PzVFm_Y0n9Ng7MGSEza5eY_mNVtr7Z96cwWkdmSOjlTzdFliNtA-U1l-VwfsFPwBdZlN6J5Hr7OkGR-qfzgscLWAPOdQnBnUjOtC4TZaWhDmMwxBkPB2SvudmuxNAh9t-jNUILoPfgl1AqvrjEzLH9X_nsPu7wayl6azQrfLbhxsyibSbOAtkdIdB15jO1Zx6X9M74jJuy_f7MAuozt0r33sUunE6yrXaqX32Aa4rbPPcgRhvQUt1L8hPqvbPPeFpRDvwDy_Rzys0t9v3bu3VduztdhqiXFZmZvFjPk5uPJ4qkBHGyAQgilZZlZ7JMNXxcbWHc0dxE8L00wiqa4gR-mylsAptCoLgHsX_EulvJ-Tq7ZEjh0BFiDl39GosnmclpUhhkkw6GHN7zDEtXOmoD8LvgMHJmvvfKQHOxl-55YFNoggeDsa0FgjM1zqO3O_mpFNDGBUxGXiHpDVnxVDjuyRwAAh8h_PJYa4kBOcOTtoYuZAAo2fLu6h6OFEkC7cqQw2FqcBdQt0ERFmQuc6VMH6RkwXHBl-sq6lrYdMNqed1-A"
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


    fun removeSavedPost(
        postId: ArrayList<Int>
    ) {
        viewModelScope.launch {
            _removeSavedPostResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.removeSavedPost(
                        "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNDZlNmM4OGI2MDFjY2YwYWJkMDQ4ZmQwYTg1NDNmY2ZiY2FiOTlkNjdjYjQ2MzhkODAwODVkYTZmMGEwMjEyMjQ2YjUxOTg4NmZlY2JlMzgiLCJpYXQiOjE2MTI5NTA0NDUsIm5iZiI6MTYxMjk1MDQ0NSwiZXhwIjoxNjQ0NDg2NDQ1LCJzdWIiOiIxNzciLCJzY29wZXMiOltdfQ.BMqYqfg9zV62zaNRf49KHGq2Va7h9A5XQ9qSm4P0SAK-CFAfM9MD4htDoBdN05U8qQLoVD5g1pDPLLAwtxxndwMgTVP9IvoqurXlnCldGJj_zIyH0zhEGhpwbNa1PzVFm_Y0n9Ng7MGSEza5eY_mNVtr7Z96cwWkdmSOjlTzdFliNtA-U1l-VwfsFPwBdZlN6J5Hr7OkGR-qfzgscLWAPOdQnBnUjOtC4TZaWhDmMwxBkPB2SvudmuxNAh9t-jNUILoPfgl1AqvrjEzLH9X_nsPu7wayl6azQrfLbhxsyibSbOAtkdIdB15jO1Zx6X9M74jJuy_f7MAuozt0r33sUunE6yrXaqX32Aa4rbPPcgRhvQUt1L8hPqvbPPeFpRDvwDy_Rzys0t9v3bu3VduztdhqiXFZmZvFjPk5uPJ4qkBHGyAQgilZZlZ7JMNXxcbWHc0dxE8L00wiqa4gR-mylsAptCoLgHsX_EulvJ-Tq7ZEjh0BFiDl39GosnmclpUhhkkw6GHN7zDEtXOmoD8LvgMHJmvvfKQHOxl-55YFNoggeDsa0FgjM1zqO3O_mpFNDGBUxGXiHpDVnxVDjuyRwAAh8h_PJYa4kBOcOTtoYuZAAo2fLu6h6OFEkC7cqQw2FqcBdQt0ERFmQuc6VMH6RkwXHBl-sq6lrYdMNqed1-A",
                        postId
                    ).let {
                        if (it.isSuccessful) {
                            _removeSavedPostResponse.postValue(Resource.success(it.body()!!))

                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _removeSavedPostResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _removeSavedPostResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _removeSavedPostResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _removeSavedPostResponse.postValue(
                Resource.error(
                    "No internet connection",
                    null
                )
            )
        }
    }

}