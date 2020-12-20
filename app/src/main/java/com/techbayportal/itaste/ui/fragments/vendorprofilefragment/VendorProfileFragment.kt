package com.techbayportal.itaste.ui.fragments.vendorprofilefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentVendorProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorProfileFragment : BaseFragment<FragmentVendorProfileBinding, VendorProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_vendor_profile
    override val viewModel: Class<VendorProfileViewModel>
        get() = VendorProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}