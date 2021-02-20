package com.techbayportal.itaste

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        var application: Application? = null
            private set
        val context: Context
            get() = application!!.applicationContext
        val PACKAGE_NAME: String
            get() = application!!.packageName
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        //timber initialising
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        uniqueDeviceId()
    }

    @SuppressLint("HardwareIds")
    private fun uniqueDeviceId() {
        val deviceId = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        LoginSession.getInstance().setDeviceId(deviceId)
    }

}