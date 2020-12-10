package com.techbayportal.itaste.ui.fragments.signupfragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class SignUpFragment : BaseFragment<LayoutSignupfragmentBinding, SignUpFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupfragment
    override val viewModel: Class<SignUpFragmentViewModel>
        get() = SignUpFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }





}