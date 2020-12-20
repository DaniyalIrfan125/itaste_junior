package com.techbayportal.itaste.ui.fragments.homeitembottomsheet

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentHomeItemBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeItemBottomSheetFragment : BaseFragment<FragmentHomeItemBottomSheetBinding, HomeItemBottomSheetViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_home_item_bottom_sheet
    override val viewModel: Class<HomeItemBottomSheetViewModel>
        get() = HomeItemBottomSheetViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}