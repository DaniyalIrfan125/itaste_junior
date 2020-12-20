package com.techbayportal.itaste.ui.fragments.home1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentHome1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home1Fragment : BaseFragment<FragmentHome1Binding, FragmentHome1ViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_home1
    override val viewModel: Class<FragmentHome1ViewModel>
        get() = FragmentHome1ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}