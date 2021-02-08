package com.techbayportal.itaste.ui.fragments.forgotpasswordfragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutForgotpasswordfragmentBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.*
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.btn_next
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.ed_phoneNumber
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.et_country_code
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<LayoutForgotpasswordfragmentBinding, ForgotPasswordFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_forgotpasswordfragment
    override val viewModel: Class<ForgotPasswordFragmentViewModel>
        get() = ForgotPasswordFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    val resetPasswordWithPhoneNumber: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fieldTextWatcher()
    }

    private fun fieldTextWatcher() {
        ed_phoneNumber.doOnTextChanged { text, start, before, count ->
            tv_errorForgotPassword.visibility = View.GONE
            ed_phoneNumber.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }

    fun validationsCheck() {
        if (!TextUtils.isEmpty(ed_phoneNumber.text)) {
                //Navigation.findNavController(btn_next).navigate(R.id.action_forgotPasswordFragment_to_otpverificationFragment)
                sharedViewModel.verifyOtpHoldPhoneNumber = ed_phoneNumber.text.toString()
                mViewModel.forgotPasswordApiCall(et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString())

        } else {
            tv_errorForgotPassword.text = getString(R.string.Pleasewritephonenumber)

            tv_errorForgotPassword.visibility = View.VISIBLE
            ed_forgotPasswordEmail.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            //tv_errorForgotPassword.text = getString(R.string.PleasewriteUsername)

        }

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            validationsCheck()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_backForgot)
                .popBackStack()
        })
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.forgotPassword.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    sharedViewModel.verifyOtpHoldPhoneNumber =
                        et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()
                    sharedViewModel.isForGotVerify = true
                    //   Navigation.findNavController(mView).navigate(R.id.action_forgotFragment_to_verifyOtpFragment)
                    Navigation.findNavController(btn_next)
                        .navigate(R.id.action_forgotPasswordFragment_to_otpverificationFragment)

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }
}