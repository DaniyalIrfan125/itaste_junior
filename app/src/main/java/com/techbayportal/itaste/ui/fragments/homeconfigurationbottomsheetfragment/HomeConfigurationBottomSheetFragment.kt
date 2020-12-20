package com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentHomeConfigurationBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeConfigurationBottomSheetFragment : BaseFragment<FragmentHomeConfigurationBottomSheetBinding, HomeConfigurationBottomSheetFragmentViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_home_configuration_bottom_sheet
    override val viewModel: Class<HomeConfigurationBottomSheetFragmentViewModel>
        get() = HomeConfigurationBottomSheetFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}