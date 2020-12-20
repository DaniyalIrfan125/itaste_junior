package com.techbayportal.itaste.ui.fragments.changelanguagefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentChangeLanguageBinding

class ChangeLanguageFragment : BaseFragment<FragmentChangeLanguageBinding, ChangeLanguageViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_change_language
    override val viewModel: Class<ChangeLanguageViewModel>
        get() = ChangeLanguageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

}