package com.techbayportal.itaste.ui.fragments.welcomefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutWelcomefragmentBinding

class Welcomefragment : BaseFragment<LayoutWelcomefragmentBinding, WelcomeViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_welcomefragment
    override val viewModel: Class<WelcomeViewModel>
        get() = WelcomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}