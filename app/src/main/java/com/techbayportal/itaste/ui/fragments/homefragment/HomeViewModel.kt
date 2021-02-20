package com.techbayportal.itaste.ui.fragments.homefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import com.techbayportal.itaste.utils.extractErrorMessage
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val dataStoreProvider: DataStoreProvider

) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

     val  onHomeConfigButtonClicked = SingleLiveEvent<Any>()
    val tempClicked = SingleLiveEvent<Any>()

    val _getAllCountriesResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val getCountriesResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _getAllCountriesResponse

    val _updateUserLocationResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val updateUserLocationResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _updateUserLocationResponse

    val _blockAccountResponse = MutableLiveData<Resource<BlockVendorResponse>>()
    val blockAccountResponse: LiveData<Resource<BlockVendorResponse>>
        get() = _blockAccountResponse

    val _getHomeScreenResponse = MutableLiveData<Resource<GetHomeScreenResponse>>()
    val getHomeScreenResponse: LiveData<Resource<GetHomeScreenResponse>>
        get() = _getHomeScreenResponse

    val _getHomeScreenForGuestResponse = MutableLiveData<Resource<GetHomeScreenResponse>>()
    val getHomeScreenForGuestResponse: LiveData<Resource<GetHomeScreenResponse>>
        get() = _getHomeScreenForGuestResponse

    val _getReportBugResponse = MutableLiveData<Resource<SuccessResponse>>()
    val getReportBugResponse: LiveData<Resource<SuccessResponse>>
        get() = _getReportBugResponse

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

    private val _logoutResponse = MutableLiveData<Resource<SuccessResponse>>()
    val logoutResponse: LiveData<Resource<SuccessResponse>>
        get() = _logoutResponse

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

    fun hitBlockAccountApi(vendorId: Int) {
        viewModelScope.launch {
            _blockAccountResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.blockVendor("Bearer ${loginSession!!.data.access_token}", vendorId).let {
                        if (it.isSuccessful) {
                            _blockAccountResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _blockAccountResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _blockAccountResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _blockAccountResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _blockAccountResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitGetHomeScreenInfoApi(fcm_token : String) {
        viewModelScope.launch {
            _getHomeScreenResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getHomeScreenInfo("Bearer ${loginSession!!.data.access_token}", fcm_token).let {
                        if (it.isSuccessful) {
                            _getHomeScreenResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getHomeScreenResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getHomeScreenResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getHomeScreenResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getHomeScreenResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitGetHomeScreenInfoApiForGuest(fcm_token : String) {
        viewModelScope.launch {
            _getHomeScreenForGuestResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getHomeScreenInfo("", fcm_token).let {
                        if (it.isSuccessful) {
                            _getHomeScreenForGuestResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getHomeScreenForGuestResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getHomeScreenForGuestResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getHomeScreenForGuestResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getHomeScreenForGuestResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitLogout() {
        viewModelScope.launch {
            _logoutResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.logout("Bearer ${loginSession!!.data.access_token}" ).let {
                        if (it.isSuccessful) {
                            // clearUserObj()
                            _logoutResponse.postValue(Resource.success(it.body()!!))
                        }else if (it.code() == 500 || it.code() == 404 || it.code() == 400) {
                            _logoutResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _logoutResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(errorMessagesJson), null))
                        }
                    }
                } catch (e: Exception) {
                    _logoutResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _logoutResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    fun hitReportBugApi(message : String) {
        viewModelScope.launch {
            _getReportBugResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.reportBug("Bearer ${loginSession!!.data.access_token}", message).let {
                        if (it.isSuccessful) {
                            _getReportBugResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getReportBugResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getReportBugResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getReportBugResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getReportBugResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun setFcm(token: String) {
        viewModelScope.launch {
            dataStoreProvider.setFcm(token)
        }
    }
}
