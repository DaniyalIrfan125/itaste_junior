package com.techbayportal.itaste.ui.fragments.selectusertypefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSelectusertypefragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectUserTypeFragment : BaseFragment<LayoutSelectusertypefragmentBinding,SelectUserTypeViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_selectusertypefragment
    override val viewModel: Class<SelectUserTypeViewModel>
        get() = SelectUserTypeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}