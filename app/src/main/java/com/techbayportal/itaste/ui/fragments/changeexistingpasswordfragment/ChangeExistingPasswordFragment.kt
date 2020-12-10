package com.techbayportal.itaste.ui.fragments.changeexistingpasswordfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutChangeexistingpasswordfragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeExistingPasswordFragment :
    BaseFragment<LayoutChangeexistingpasswordfragmentBinding, ChangeExistingPasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changeexistingpasswordfragment
    override val viewModel: Class<ChangeExistingPasswordViewModel>
        get() = ChangeExistingPasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}