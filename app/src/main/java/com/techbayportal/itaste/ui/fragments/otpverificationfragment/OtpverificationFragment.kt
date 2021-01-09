package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.Data
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutOtpverificationfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_loginfragment.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mView = view
        initialising()
        fieldTextWatcher()
        clickListener(mView)
    }
    fun initialising() {
        otpCountDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_expiryTime.text = counter.toString() + " sec"
                counter--
            }

            override fun onFinish() {
                tv_expiryTime.visibility = View.GONE
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
                mViewModel.verifyOTP(
                    Integer.parseInt(otp_view.text.toString()),
                    sharedViewModel.verifyOtpHoldPhoneNumber,
                    AppConstants.VerifyOTPTypeKeys.FORGOT_PASSWORD
                )
            } else {
                mViewModel.verifyOTP(
                    Integer.parseInt(otp_view.text.toString()),
                    sharedViewModel.verifyOtpHoldPhoneNumber,
                    AppConstants.VerifyOTPTypeKeys.SIGN_UP
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
            tv_expiryTime.visibility = View.VISIBLE
            counter = 60
            otpCountDownTimer.cancel()
            otpCountDownTimer.start()
            tv_resendOtp.isEnabled = false
            sharedViewModel.verifyOtpHoldPhoneNumber?.let {
                //key is set "forget-password" as per provided by backend
                mViewModel.hitResentOtp(it, AppConstants.VerifyOTPTypeKeys.FORGOT_PASSWORD)
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

                    } else {
                        if (it.data!!.data == null){
                            Toast.makeText(requireContext(), "Login Response is Empty", Toast.LENGTH_SHORT).show()
                        }else{
                            val data = it.data.data!!

                            val userObje = LoginResponse(
                                "",
                                Data(
                                    "" +data.id,
                                    data.first,
                                    data.last,
                                    data.username,
                                    data.phone,
                                    data.email,
                                    data.profile_img,
                                    data.role,
                                    data.access_token
                                ), ""
                            )
                           // mViewModel.saveUserObj(userObje)
                            navigateToMainScreen()
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    counter = 60
                    otpCountDownTimer.cancel()
                    tv_expiryTime.visibility = View.GONE
                    tv_resendOtp.isEnabled = true
                    DialogClass.errorDialog(requireContext(), it.message!!)
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
                    tv_resendOtp.isEnabled = false
//                    if(counter in 0..10){
//                        tv_expiryTime.setTextColor(resources.getColor(R.color.colorErrorRed))
//                    }else{
//                        tv_expiryTime.setTextColor(resources.getColor(R.color.otpTimerColor))
//                    }

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    counter = 60
                    otpCountDownTimer.cancel()
                    tv_expiryTime.visibility = View.GONE
                    tv_resendOtp.isEnabled = true
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })
    }


}