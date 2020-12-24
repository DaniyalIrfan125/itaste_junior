package com.techbayportal.itaste.ui.fragments.otpverificationfragment

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
import com.techbayportal.itaste.databinding.LayoutOtpverificationfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.*
import kotlinx.android.synthetic.main.layout_otpverificationfragment.*
import kotlinx.android.synthetic.main.layout_otpverificationfragment.btn_next


@AndroidEntryPoint
class OtpverificationFragment :
    BaseFragment<LayoutOtpverificationfragmentBinding, OtpVerificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_otpverificationfragment
    override val viewModel: Class<OtpVerificationViewModel>
        get() = OtpVerificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTextWatcher()

    }


    private fun fieldTextWatcher() {
        otp_view.doOnTextChanged { text, start, before, count ->
            tv_otpError.visibility = View.GONE
        }
    }

    fun validateFields(){
        if (!TextUtils.isEmpty(otp_view.text)) {
            Navigation.findNavController(btn_next).navigate(R.id.action_otpverificationFragment_to_changePasswordFragment)
        } else {
            tv_otpError.visibility = View.VISIBLE
            tv_otpError.text = "Please write Otp!"
        }
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            validateFields()

        })

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_back)
                .popBackStack()
        })
    }
}