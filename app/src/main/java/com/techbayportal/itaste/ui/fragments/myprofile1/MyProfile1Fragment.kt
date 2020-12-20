package com.techbayportal.itaste.ui.fragments.myprofile1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMyProfile1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfile1Fragment : BaseFragment<FragmentMyProfile1Binding, MyProfile1ViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_my_profile1
    override val viewModel: Class<MyProfile1ViewModel>
        get() = MyProfile1ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}