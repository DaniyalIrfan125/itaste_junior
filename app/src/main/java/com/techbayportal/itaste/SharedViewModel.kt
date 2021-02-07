package com.techbayportal.itaste

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.GetAllCountriesResponse
import com.techbayportal.itaste.data.models.GetCategoriesResponse
import com.techbayportal.itaste.data.models.UserModel
import java.io.File


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    var verifyOtpHoldPhoneNumber = ""
    var verifyOtpHoldNewPhoneNumber = ""
    var isForGotVerify = false
    var isUpdatePhone = false
    var userType = "user"
    var otpVerifyCode = ""
    var userModel = UserModel()
    val isDarkMode :Boolean = false
    val localProfilePic: File? = null
    var selectedPostImageFile = MutableLiveData<File>()
    var isEditPost : Boolean = false
    var isEditBottomSheetClicked = MutableLiveData<Boolean>(false)
    var isPostUpdated = MutableLiveData<Boolean>()
    var categoriesResponse : MutableLiveData<List<GetCategoriesResponse.Data>> = MutableLiveData()
//    val countriesList = ArrayList<GetAllCountriesData>()


    //var isSelectedCountryId : MutableLiveData<Int>? = null
//    val isSelectedCountryId: LiveData<Int>


    val _isSelectedCountryId = MutableLiveData<Int>()
    val isSelectedCountryId: LiveData<Int>
        get() = _isSelectedCountryId

    val _homeConfigBottomSheetClickId = MutableLiveData<Int>()
    val homeItemBottomSheetClickId = MutableLiveData<Int>()
   // val homeConfigBottomSheetClickId: LiveData<Int>
       // get() = _homeConfigBottomSheetClickId

    val _countriesList = MutableLiveData<GetAllCountriesResponse>()
    val countriesList: LiveData<GetAllCountriesResponse>
        get() = _countriesList

    val reportBugButtonsClicked = MutableLiveData<Int>()
    var bugReportMessage = ""
    //var bugReportMessage = MutableLiveData<String>()

    var userUpdatedCountryId : Int = 0

    var test = false;
    var testId :String = "0"
}