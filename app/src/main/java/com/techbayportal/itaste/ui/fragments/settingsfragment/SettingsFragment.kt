package com.techbayportal.itaste.ui.fragments.settingsfragment


import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: Class<SettingsFragmentViewModel>
        get() = SettingsFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}