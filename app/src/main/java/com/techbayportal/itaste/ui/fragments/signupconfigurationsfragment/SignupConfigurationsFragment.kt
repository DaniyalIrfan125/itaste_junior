package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupconfigurationsfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class SignupConfigurationsFragment :
    BaseFragment<LayoutSignupconfigurationsfragmentBinding, SignUpConfigurationsViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupconfigurationsfragment
    override val viewModel: Class<SignUpConfigurationsViewModel>
        get() = SignUpConfigurationsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show()
        })

        mViewModel.onDarkModeButtonClicked.observe(this, Observer {
            Toast.makeText(context, "Dark Mode", Toast.LENGTH_SHORT).show()
        })

        mViewModel.onLightModeButtonClicked.observe(this, Observer {
            Toast.makeText(context, "Light Mode", Toast.LENGTH_SHORT).show()
        })
    }
}