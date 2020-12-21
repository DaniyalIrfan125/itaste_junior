package com.techbayportal.itaste.ui.fragments.userprofilefragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding, UserProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_profile
    override val viewModel: Class<UserProfileViewModel>
        get() = UserProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}