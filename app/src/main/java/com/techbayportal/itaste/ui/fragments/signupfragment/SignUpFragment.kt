package com.techbayportal.itaste.ui.fragments.signupfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupfragmentBinding

class SignUpFragment : BaseFragment<LayoutSignupfragmentBinding, SignUpViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupfragment
    override val viewModel: Class<SignUpViewModel>
        get() = SignUpViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}