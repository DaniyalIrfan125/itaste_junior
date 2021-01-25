package com.techbayportal.itaste.ui.fragments.signupvendorfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch
import java.io.File

class SignupVendorViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val _getAllCountriesResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val getCountriesResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _getAllCountriesResponse

    val _getAllCitiesResponse = MutableLiveData<Resource<GetAllCitiesResponse>>()
    val _getCitiesResponse: LiveData<Resource<GetAllCitiesResponse>>
        get() = _getAllCitiesResponse

    private val _signUpVendorResponse = MutableLiveData<Resource<SignUpResponse>>()
    val signUpVendorResponse: LiveData<Resource<SignUpResponse>>
        get() = _signUpVendorResponse

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSignUpVendorButtonClicked = SingleLiveEvent<Any>()
    val onChooseCountrySpinnerClicked = SingleLiveEvent<Any>()
    val onChooseCitySpinnerClicked = SingleLiveEvent<Any>()

    val onServiceTypeRadioButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onSignUpVendorButtonClicked() {
        onSignUpVendorButtonClicked.call()
    }

//    fun onChooseCountrySpinnerClicked() {
//        onChooseCountrySpinnerClicked.call()
//    }
//
//    fun onChooseCitySpinnerClicked() {
//        onChooseCitySpinnerClicked.call()
 //   }

    fun onServiceTypeRadioButtonClicked() {
        onServiceTypeRadioButtonClicked.call()
    }

    fun getAllCountries() {
        viewModelScope.launch {
            _getAllCountriesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllCountriesForHome("").let {
                        if (it.isSuccessful) {
                            _getAllCountriesResponse.postValue(Resource.success(it.body()!!))
                        } else _getAllCountriesResponse.postValue(Resource.error(it.message(), null))
                    }
                } catch (e: Exception) {
                    _getAllCountriesResponse.postValue(Resource.error(""+e.message, null))
                }
            } else _getAllCountriesResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getAllCities(countryId: Int) {
        viewModelScope.launch {
            _getAllCitiesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllCities("",countryId).let {
                        if (it.isSuccessful) {
                            _getAllCitiesResponse.postValue(Resource.success(it.body()!!))
                        } else _getAllCitiesResponse.postValue(Resource.error(it.message(), null))
                    }
                } catch (e: Exception) {
                    _getAllCitiesResponse.postValue(Resource.error(""+e.message, null))
                }
            } else _getAllCitiesResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    fun signUpVendorAPICall(userModel: UserModel) {
        viewModelScope.launch {
            _signUpVendorResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.signUpVendor(userModel).let {
                    if (it.isSuccessful) {
                        _signUpVendorResponse.postValue(Resource.success(it.body()!!))
                    } else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                        _signUpVendorResponse.postValue(Resource.error(it.message(), null))
                    } else {
                        val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                        _signUpVendorResponse.postValue(
                            Resource.error(
                                extractErrorMessage(
                                    errorMessagesJson
                                ), null
                            )
                        )
                    }
                }
            } else _signUpVendorResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    init {
        getAllCountries()
    }
}