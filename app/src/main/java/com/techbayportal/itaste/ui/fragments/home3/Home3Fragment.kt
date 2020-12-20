package com.techbayportal.itaste.ui.fragments.home3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentHome3Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home3Fragment : BaseFragment<FragmentHome3Binding, Home3ViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_home3
    override val viewModel: Class<Home3ViewModel>
        get() = Home3ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}