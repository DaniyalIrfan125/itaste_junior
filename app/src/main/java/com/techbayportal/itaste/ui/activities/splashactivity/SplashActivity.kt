package com.techbayportal.itaste.ui.activities.splashactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.utils.LoginSession

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val dataStoreProvider = DataStoreProvider(this)

        dataStoreProvider.userObjFlow.asLiveData().observe(this, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    getLoginSession(it)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val moveToMain = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(moveToMain)
                        finish()
                    }, 100)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val moveToAuth =
                            Intent(this@SplashActivity, SignupActivity::class.java)
                        startActivity(moveToAuth)
                        finish()
                    }, 100)
                }
            }
            if(it == null){
                Handler(Looper.getMainLooper()).postDelayed({
                    val moveToAuth =
                        Intent(this@SplashActivity, SignupActivity::class.java)
                    startActivity(moveToAuth)
                    finish()
                }, 100)

            }
        })


    }

    fun getLoginSession(resp:String){
        val gson = Gson()
        val json: String = resp
        val loginResponse = gson.fromJson(json, LoginResponse::class.java)
        LoginSession.getInstance().setLoginResponse(loginResponse)
    }

}