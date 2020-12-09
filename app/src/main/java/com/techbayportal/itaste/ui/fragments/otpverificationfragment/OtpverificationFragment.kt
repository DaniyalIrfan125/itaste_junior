package com.techbayportal.itaste.ui.fragments.otpverificationfragment

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutOtpverificationfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_otpverificationfragment.*


@AndroidEntryPoint
class OtpverificationFragment :
    BaseFragment<LayoutOtpverificationfragmentBinding, OtpVerificationViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_otpverificationfragment
    override val viewModel: Class<OtpVerificationViewModel>
        get() = OtpVerificationViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_next).navigate(R.id.action_otpverificationFragment_to_changePasswordFragment)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_back)
                .popBackStack()
        })
    }
}