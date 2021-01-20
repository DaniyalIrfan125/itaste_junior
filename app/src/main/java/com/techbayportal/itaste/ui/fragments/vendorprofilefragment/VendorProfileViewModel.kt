package com.techbayportal.itaste.ui.fragments.vendorprofilefragment

import android.provider.ContactsContract
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

class VendorProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onBackButtonClicked = SingleLiveEvent<Any>()
    var onSaveChangesButtonClicked = SingleLiveEvent<Any>()
    val onProfilePicChangeTextClicked = SingleLiveEvent<Any>()

    val _getVendorPersonalProfileResponse = MutableLiveData<Resource<VendorPersonalProfileResponse>>()
    val getVendorPersonalProfileResponse: LiveData<Resource<VendorPersonalProfileResponse>>
        get() = _getVendorPersonalProfileResponse

    val _getUpdateVendorPersonalProfileResponse = MutableLiveData<Resource<SuccessResponse>>()
    val getUpdateVendorPersonalProfileResponse: LiveData<Resource<SuccessResponse>>
        get() = _getUpdateVendorPersonalProfileResponse

    val _getAllCountriesResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val getCountriesResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _getAllCountriesResponse

    val _getAllCitiesResponse = MutableLiveData<Resource<GetAllCitiesResponse>>()
    val getCitiesResponse: LiveData<Resource<GetAllCitiesResponse>>
        get() = _getAllCitiesResponse

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onSaveChangesButtonClicked(){
        onSaveChangesButtonClicked.call()
    }

    fun onProfilePicChangeTextClicked(){
        onProfilePicChangeTextClicked.call()
    }


    fun hitGetVendorPersonalProfile() {
        viewModelScope.launch {
            _getVendorPersonalProfileResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getVendorPersonalProfile("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getVendorPersonalProfileResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getVendorPersonalProfileResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getVendorPersonalProfileResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getVendorPersonalProfileResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getVendorPersonalProfileResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitUpdateVendorPersonalProfile( first_name:String, last_name:String, bio:String, phone :String ,email: String, profilePic:File?, country_id: String, city_id: String) {
        viewModelScope.launch {
            _getUpdateVendorPersonalProfileResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.updateVendorPersonalProfile("Bearer ${loginSession!!.data.access_token}", first_name,last_name,bio,phone,email,profilePic, country_id, city_id).let {
                        if (it.isSuccessful) {
                            _getUpdateVendorPersonalProfileResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getUpdateVendorPersonalProfileResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getUpdateVendorPersonalProfileResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getUpdateVendorPersonalProfileResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getUpdateVendorPersonalProfileResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun getAllCountries() {
        viewModelScope.launch {
            _getAllCountriesResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllCountriesForHome("Bearer ${loginSession!!.data.access_token}").let {
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
                    mainRepository.getAllCities("Bearer ${loginSession!!.data.access_token}",countryId).let {
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

    init {
        getAllCountries()
    }
}