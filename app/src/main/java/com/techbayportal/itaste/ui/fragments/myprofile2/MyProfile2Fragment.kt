package com.techbayportal.itaste.ui.fragments.myprofile2

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMyProfile2Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfile2Fragment : BaseFragment<FragmentMyProfile2Binding, MyProfile2ViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_my_profile2
    override val viewModel: Class<MyProfile2ViewModel>
        get() = MyProfile2ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}