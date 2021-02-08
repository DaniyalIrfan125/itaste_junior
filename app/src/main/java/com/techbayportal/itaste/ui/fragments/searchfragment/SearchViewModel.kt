package com.techbayportal.itaste.ui.fragments.searchfragment

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

class SearchViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val loginSession = LoginSession.getInstance().getLoginResponse()

    val onResetButtonClicked = SingleLiveEvent<Any>()
    val onApplyFilterButtonClicked = SingleLiveEvent<Any>()

    val _getAllCountriesResponse = MutableLiveData<Resource<GetAllCountriesResponse>>()
    val getCountriesResponse: LiveData<Resource<GetAllCountriesResponse>>
        get() = _getAllCountriesResponse

    val _getAllCitiesResponse = MutableLiveData<Resource<GetAllCitiesResponse>>()
    val _getCitiesResponse: LiveData<Resource<GetAllCitiesResponse>>
        get() = _getAllCitiesResponse

    val _getAllCategoriesResponse = MutableLiveData<Resource<GetAllCategoriesResponse>>()
    val getAllCategoriesResponse: LiveData<Resource<GetAllCategoriesResponse>>
        get() = _getAllCategoriesResponse

    val _getAllSearchPostsResponse = MutableLiveData<Resource<SearchAndFilterResponse>>()
    val getAllSearchPostsResponse: LiveData<Resource<SearchAndFilterResponse>>
        get() = _getAllSearchPostsResponse

    val _getSearchAndFilterResponse = MutableLiveData<Resource<SearchAndFilterResponse>>()
    val getSearchAndFilterResponse: LiveData<Resource<SearchAndFilterResponse>>
        get() = _getSearchAndFilterResponse


    val _getSearchResponse = MutableLiveData<Resource<SearchAndFilterResponse>>()
    val getSearchResponse: LiveData<Resource<SearchAndFilterResponse>>
        get() = _getSearchResponse



    fun onResetButtonClicked() {
        onResetButtonClicked.call()
    }
    fun onApplyFilterButtonClicked() {
        onApplyFilterButtonClicked.call()
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

    fun hitGetAllCategoriesApi() {
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

    fun hitGetAllSearchPostsDataApi() {
        viewModelScope.launch {
            _getAllSearchPostsResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.getAllSearchPostsApi("Bearer ${loginSession!!.data.access_token}").let {
                        if (it.isSuccessful) {
                            _getAllSearchPostsResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getAllSearchPostsResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getAllSearchPostsResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getAllSearchPostsResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getAllSearchPostsResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }


    fun hitApplyFiltersApi(keyword : String, country_id:String, city_id :String, category_id :String) {
        viewModelScope.launch {
            _getSearchAndFilterResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.searchAndFilterApi("Bearer ${loginSession!!.data.access_token}", keyword, country_id, city_id, category_id).let {
                        if (it.isSuccessful) {
                            _getSearchAndFilterResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getSearchAndFilterResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getSearchAndFilterResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getSearchAndFilterResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getSearchAndFilterResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }

    fun hitSearchApi(keyword : String) {
        viewModelScope.launch {
            _getSearchResponse.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {
                    mainRepository.searchApi("Bearer ${loginSession!!.data.access_token}", keyword).let {
                        if (it.isSuccessful) {
                            _getSearchResponse.postValue(Resource.success(it.body()!!))
                        } else if (it.code() == 500 || it.code() == 404 || it.code() == 400 || it.code() == 401) {
                            _getSearchResponse.postValue(Resource.error(it.message(), null))
                        } else {
                            val errorMessagesJson = it.errorBody()?.source()?.buffer?.readUtf8()!!
                            _getSearchResponse.postValue(
                                Resource.error(
                                    extractErrorMessage(
                                        errorMessagesJson
                                    ), null
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    _getSearchResponse.postValue(Resource.error("" + e.message, null))
                }
            } else _getSearchResponse.postValue(Resource.error("No Internet Connection", null))
        }
    }



}