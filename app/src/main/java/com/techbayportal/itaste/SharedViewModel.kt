package com.techbayportal.itaste

import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.models.UserModel
import java.io.File


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    var verifyOtpHoldPhoneNumber = ""
    var isForGotVerify = false
    var userType = "user"
    var otpVerifyCode = ""
    var userModel = UserModel()
    val isDarkMode :Boolean = false
    val localProfilePic: File? = null

}