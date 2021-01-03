package com.techbayportal.itaste.ui.fragments.signupfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.SignUpResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch
import java.io.File

class SignUpFragmentViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSignUpButtonClicked = SingleLiveEvent<Any>()
    val onProfilePicTextClicked = SingleLiveEvent<Any>()

    private val _signUpResponse = MutableLiveData<Resource<SignUpResponse>>()
    val signUpResponse: LiveData<Resource<SignUpResponse>>
        get() = _signUpResponse

    fun signUpAPICall(
        first: String,
        last: String,
        username: String,
        phone: String,
        profilePic: File,
        email: String,
        password: String,
        role: String
    ) {
        viewModelScope.launch {
            _signUpResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.signUp(
                    first,
                    last,
                    username,
                    phone,
                    profilePic,
                    email,
                    password,
                    role
                ).let {
                    if (it.isSuccessful) {
                        _signUpResponse.postValue(Resource.success(it.body()!!))
                    } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                        _signUpResponse.postValue(Resource.error(it.message(), null))
                    } else {
                        val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                        _signUpResponse.postValue(
                            Resource.error(
                                extractErrorMessage(
                                    errorMessagesJson
                                ), null
                            )
                        )
                    }
                }
            } else _signUpResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onSignUpButtonClicked() {
        onSignUpButtonClicked.call()
    }

    fun onProfilePicTextClicked() {
        onProfilePicTextClicked.call()
    }


}