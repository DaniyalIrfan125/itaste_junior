package com.techbayportal.itaste.ui.fragments.selectaccounttypefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutAccounttypefragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAccountTypeFragment : BaseFragment<LayoutAccounttypefragmentBinding,SelectAccountTypeViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_accounttypefragment
    override val viewModel: Class<SelectAccountTypeViewModel>
        get() = SelectAccountTypeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}