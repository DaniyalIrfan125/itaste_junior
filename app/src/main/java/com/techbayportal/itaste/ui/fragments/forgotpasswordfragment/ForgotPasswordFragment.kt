package com.techbayportal.itaste.ui.fragments.forgotpasswordfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutForgotpasswordfragmentBinding

class ForgotPasswordFragment : BaseFragment<LayoutForgotpasswordfragmentBinding,ForgotPasswordFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_forgotpasswordfragment
    override val viewModel: Class<ForgotPasswordFragmentViewModel>
        get() = ForgotPasswordFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}