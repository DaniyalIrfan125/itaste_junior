package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutOtpverificationfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_otpverificationfragment.*
import kotlinx.android.synthetic.main.layout_otpverificationfragment.btn_next


@AndroidEntryPoint
class OtpverificationFragment : BaseFragment<LayoutOtpverificationfragmentBinding, OtpVerificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_otpverificationfragment
    override val viewModel: Class<OtpVerificationViewModel>
        get() = OtpVerificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var mView: View

    lateinit var otpCountDownTimer: CountDownTimer
    var counter = 60

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialising()
        this.mView = view
        fieldTextWatcher()
        clickListener(mView)
    }
    fun initialising() {
        otpCountDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_expiryTime.text = "$counter sec"
                counter--

                if(counter in 0..9){
                    tv_expiryTime.setTextColor(resources.getColor(R.color.colorErrorRed))
                    iv_otp_timer_clock.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorErrorRed))
                }else{
                    tv_expiryTime.setTextColor(resources.getColor(R.color.otpTimerColor))
                    iv_otp_timer_clock.setColorFilter(ContextCompat.getColor(requireContext(), R.color.otpTimerColor))
                }

            }

            override fun onFinish() {
                tv_expiryTime.visibility = View.GONE
                iv_otp_timer_clock.visibility  = View.GONE
                tv_resendOtp.isEnabled = true
            }
        }
        otpCountDownTimer.start()

    }


    private fun fieldTextWatcher() {
        otp_view.doOnTextChanged { text, start, before, count ->
            tv_otpError.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        otpCountDownTimer.cancel()
    }

    fun validateFields() {
        if (!TextUtils.isEmpty(otp_view.text)) {
            //
            // Navigation.findNavController(btn_next).navigate(R.id.action_otpverificationFragment_to_changePasswordFragment)
            //call api
            if (sharedViewModel.isForGotVerify) {
                mViewModel.verifyOTP(Integer.parseInt(otp_view.text.toString()), sharedViewModel.verifyOtpHoldPhoneNumber, AppConstants.VerifyOTPTypeKeys.FORGOT_PASSWORD
                )
            } else if(sharedViewModel.isUpdatePhone){
                mViewModel.hitVerifyOtpForPhone(Integer.parseInt(otp_view.text.toString()), sharedViewModel.verifyOtpHoldNewPhoneNumber, AppConstants.VerifyOTPTypeKeys.UPDATE_PHONE )
            }

            else {
                mViewModel.verifyOTP(Integer.parseInt(otp_view.text.toString()), sharedViewModel.verifyOtpHoldPhoneNumber, AppConstants.VerifyOTPTypeKeys.SIGN_UP
                )
            }
        } else {
            tv_otpError.visibility = View.VISIBLE
            tv_otpError.text = getString(R.string.PleasewriteOtp)
        }
    }

    fun clickListener(view: View) {
        btn_back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        btn_next.setOnClickListener {
            validateFields()
        }

        tv_resendOtp.setOnClickListener {
            iv_otp_timer_clock.visibility = View.GONE

            counter = 60
            otpCountDownTimer.cancel()
            tv_resendOtp.isEnabled = false
            otp_view.text = null


            sharedViewModel.verifyOtpHoldPhoneNumber.let {
                //key is set "forget-password" as per provided by backend

                if (sharedViewModel.isForGotVerify) {
                    mViewModel.hitResentOtp(it, AppConstants.VerifyOTPTypeKeys.FORGOT_PASSWORD)

                } else if(sharedViewModel.isUpdatePhone){
                    mViewModel.hitResentOtpForPhone(it, AppConstants.VerifyOTPTypeKeys.UPDATE_PHONE)
                } else{
                    mViewModel.hitResentOtp(it, AppConstants.VerifyOTPTypeKeys.SIGN_UP)
                }
            }
        }

    }

    /*override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            validateFields()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_back)
                .popBackStack()
        })
    }*/


    //navigate to home screen
    private fun navigateToMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.verifyOtpResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    tv_expiryTime.visibility = View.VISIBLE
                    loadingDialog.dismiss()
                    otpCountDownTimer.cancel()
                    sharedViewModel.otpVerifyCode = otp_view.text.toString()
                    if (sharedViewModel.isForGotVerify) {

                        // if it is from forgot verify
                        if (Navigation.findNavController(mView).currentDestination?.id == R.id.otpverificationFragment){
                            Navigation.findNavController(mView).navigate(R.id.action_otpverificationFragment_to_changePasswordFragment)

                        }
                        else{
                           // Toast.makeText(requireContext(), "Already there", Toast.LENGTH_SHORT).show()
                        }

                    }else if(sharedViewModel.isUpdatePhone){
                        Navigation.findNavController(mView).popBackStack()
                    }

                    else {

                        if(sharedViewModel.userType == AppConstants.UserTypeKeys.USER){
                            if (it.data!!.data == null){
                                Toast.makeText(requireContext(), "Otp Response is Empty", Toast.LENGTH_SHORT).show()
                            }else{

                                try {
                                    mViewModel.saveUserObj(it.data)
                                    sharedViewModel.testId = it.data.data.id
                                    Navigation.findNavController(mView).navigate(R.id.action_otpverificationFragment_to_signupConfigurationsFragment)
                                } catch (e: Exception) {
                                }
                            }


                        }else if(sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR){
                            if (it.data!!.data == null){
                                Toast.makeText(requireContext(), "Otp Response is Empty", Toast.LENGTH_SHORT).show()
                            }else{
                                try {
                                    mViewModel.saveUserObj(it.data)
                                    sharedViewModel.testId = it.data.data.id
                                    navigateToMainScreen()
                                } catch (e: Exception) {
                                }
                            }
                        }


                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    counter = 60
                    otpCountDownTimer.cancel()
                    tv_expiryTime.visibility = View.GONE
                    iv_otp_timer_clock.visibility = View.GONE
                    tv_resendOtp.isEnabled = true
                    otp_view.text = null
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.resentVerifyOtpResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    counter = 60
                    otpCountDownTimer.start()
                    tv_expiryTime.visibility = View.VISIBLE
                    iv_otp_timer_clock.visibility = View.VISIBLE
                    tv_resendOtp.isEnabled = false


                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    counter = 60
                    otpCountDownTimer.cancel()
                    tv_expiryTime.visibility = View.GONE
                    iv_otp_timer_clock.visibility = View.GONE
                    tv_resendOtp.isEnabled = true
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }


}