package com.techbayportal.itaste.ui.fragments.loginfragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutLoginfragmentBinding
import com.techbayportal.itaste.databinding.LayoutSecondBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_loginfragment.*
import kotlinx.android.synthetic.main.layout_signupfragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : BaseFragment<LayoutLoginfragmentBinding, LoginViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_loginfragment
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var darkMode :Boolean = false
    var fcmToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()

        mViewModel.setGuestMode(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserveDarkActivation()
        fieldTextWatcher()

        mViewModel.setGuestMode(false)
        //it shuld be in splash screen
        //if user is logi or not


       /* dataStoreProvider.userObjFlow.asLiveData().observe(this, Observer {

            if(it!!.isEmpty()){
                val gson = Gson()
                val json: String = it
                val loginResponse = gson.fromJson(json, LoginResponse::class.java)
                LoginSession.getInstance().setLoginResponse(loginResponse)
            }


        })*/
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.loginResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {

                    if(it.data != null){
                        if(it.data.data.role.equals(AppConstants.UserTypeKeys.USER,false)){
                            GlobalScope.launch {
                                mViewModel.dataStoreProvider.switchToPremium(false)
//                                dataStoreProvider.switchToPremium(false)
                            }

                        }
                    }




                    it?.let {
                        loadingDialog.dismiss()
                        mViewModel.saveUserObj(it.data!!)
                        sharedViewModel.testId = it.data.data.id
                        GlobalScope.launch {

                            mViewModel.setGuestMode(false)
                            navigateToMainActivity()
                        }
                       // sharedViewModel.verifyOtpHoldPhoneNumber = editTextEmail.text.toString()


                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    private fun fieldTextWatcher() {
        ed_enterUserName.doOnTextChanged { text, start, before, count ->
            tv_userNameError.visibility = View.GONE
            ed_enterUserName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
        ed_password.doOnTextChanged { text, start, before, count ->
            tv_passwordError.visibility = View.GONE
            ed_password.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }


    fun fieldValidationsCheck() {

        if (!TextUtils.isEmpty(ed_enterUserName.text)) {
            if (!TextUtils.isEmpty(ed_password.text) && ed_password.text.toString().length > 5 ){

               // navigateToMainActivity()
                mViewModel.loginApiCall(
                    ed_enterUserName.text.toString(),
                    ed_password.text.toString(),
                    fcmToken
                )
            } else {

                if(ed_password.text.toString().length in 1..5){
                    tv_passwordError.text = getString(R.string.password_at_least_six_character)
                }else{
                    tv_passwordError.text = getString(R.string.Pleasewritepassword)
                }
                tv_passwordError.visibility = View.VISIBLE
                ed_password.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            }

        } else {
            tv_userNameError.visibility = View.VISIBLE
            ed_enterUserName.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_userNameError.text = getString(R.string.PleasewriteUsername)
        }


    }

    private fun fetchFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Timber.d("FCM token: $token")

            if (token != null) {
                fcmToken = token
                mViewModel.setFcm(token)
            }
        })
    }


    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onLoginClicked.observe(this, Observer{
            GlobalScope.launch {
                //mViewModel.setGuestMode(false)
                fieldValidationsCheck()
            }

        })

        mViewModel.onForgotPasswordClicked.observe(this, Observer {
            GlobalScope.launch {
                mViewModel.setGuestMode(false)
                Navigation.findNavController(ed_enterUserName).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
            }


        })

        mViewModel.onSignUpClicked.observe(this, Observer {
            GlobalScope.launch {
                mViewModel.setGuestMode(false)
                Navigation.findNavController(ed_enterUserName).navigate(R.id.action_loginFragment_to_selectAccountTypeFragment2)
            }

        })

        mViewModel.onGuestModeButtonClicked.observe(this, Observer {
            GlobalScope.launch {
                mViewModel.setGuestMode(true)
                navigateToMainActivity()
            }

        })
    }


    override fun onStop() {
        super.onStop()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    override fun onStart() {
        super.onStart()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        mViewModel.dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
            //  switch_darkMode.isChecked = it
            if (it != null) {
                if(it == true){
                    iv_app_logo.setImageResource(R.drawable.app_icon_dark)
                    darkMode
                    //iv_app_logo.setBackgroundResource(R.drawable.app_icon_dark)
                }else{
                    !darkMode
                }
            }
        })

    }




}
