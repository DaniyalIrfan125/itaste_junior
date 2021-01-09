package com.techbayportal.itaste

import com.techbayportal.itaste.baseclasses.BaseViewModel


/**
 * Shared View Model class for sharing data between fragments
 */
class SharedViewModel : BaseViewModel() {

    var verifyOtpHoldPhoneNumber = ""
    var isForGotVerify = false
    var userType = "user"
    var otpVerifyCode = ""

}