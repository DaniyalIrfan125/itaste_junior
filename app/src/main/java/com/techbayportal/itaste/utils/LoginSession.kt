package com.techbayportal.itaste.utils

import com.techbayportal.itaste.data.models.LoginResponse

class LoginSession {
    private var loginResponse: LoginResponse? = null
    private var deviceId: String? = null
    private var FcmToken: String? = null
    companion object{
        private val ourInstance: LoginSession = LoginSession()
        fun getInstance(): LoginSession {
            return ourInstance
        }
    }
    private fun LoginSession() {}
    fun getLoginResponse(): LoginResponse? {
        return loginResponse
    }
    fun setLoginResponse(loginResponse: LoginResponse?) {
        this.loginResponse = loginResponse
    }
    fun getDeviceId(): String? {
        return deviceId
    }
    fun setDeviceId(deviceId: String?) {
        this.deviceId = deviceId
    }
    fun getFcmToken(): String? {
        return FcmToken
    }
    fun setFcmToken(fcmToken: String?) {
        FcmToken = fcmToken
    }

}