package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupconfigurationsfragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignupConfigurationsFragment :
    BaseFragment<LayoutSignupconfigurationsfragmentBinding, SignUpConfigurationsViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupconfigurationsfragment
    override val viewModel: Class<SignUpConfigurationsViewModel>
        get() = SignUpConfigurationsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}