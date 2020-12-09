package com.techbayportal.itaste.ui.fragments.forgotpasswordfragment

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutForgotpasswordfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.*
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.btn_next
import kotlinx.android.synthetic.main.layout_otpverificationfragment.*

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<LayoutForgotpasswordfragmentBinding,ForgotPasswordFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_forgotpasswordfragment
    override val viewModel: Class<ForgotPasswordFragmentViewModel>
        get() = ForgotPasswordFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_next).navigate(R.id.action_forgotPasswordFragment_to_otpverificationFragment)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_backForgot)
                .popBackStack()
        })
    }
}