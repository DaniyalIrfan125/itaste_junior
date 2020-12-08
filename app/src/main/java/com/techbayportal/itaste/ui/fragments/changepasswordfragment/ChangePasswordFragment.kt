package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutChangepasswordfragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<LayoutChangepasswordfragmentBinding, ChangePasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changepasswordfragment
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}