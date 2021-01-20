package com.techbayportal.itaste.ui.fragments.homefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.GetAllCountriesResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onHomeConfigButtonClicked = SingleLiveEvent<Any>()
    val tempClicked = SingleLiveEvent<Any>()

    val _getAllCountriesResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val getCountriesResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _getAllCountriesResponse

    val _updateUserLocationResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val updateUserLocationResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _updateUserLocationResponse

    fun onHomeConfigButtonClicked() {
        /*if(loginSession != null){
            if(loginSession.data.role.equals("user",true)){

            }else{

            }
        }*/
        onHomeConfigButtonClicked.call()
    }

    fun tempClicked() {
        tempClicked.call()
    }

    fun hitGetAllCountriesForHome() {
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

    fun hitUpdateUserLocationFromHome(countryId : Int) {
        viewModelScope.launch {
            _updateUserLocationResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.updateUserLocation("Bearer ${loginSession!!.data.access_token}", countryId).let {
                    if (it.isSuccessful) {
                        _updateUserLocationResponse.postValue(Resource.success(it.body()!!))
                    } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                        _updateUserLocationResponse.postValue(Resource.error(it.message(), null))
                    } else {
                        val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                        _updateUserLocationResponse.postValue(
                            Resource.error(
                                extractErrorMessage(
                                    errorMessagesJson
                                ), null
                            )
                        )
                    }
                }
            } else _updateUserLocationResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }
}