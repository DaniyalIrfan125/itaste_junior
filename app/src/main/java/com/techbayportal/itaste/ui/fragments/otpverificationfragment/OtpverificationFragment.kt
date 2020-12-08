package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutOtpverificationfragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OtpverificationFragment :
    BaseFragment<LayoutOtpverificationfragmentBinding, OtpVerificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_otpverificationfragment
    override val viewModel: Class<OtpVerificationViewModel>
        get() = OtpVerificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}