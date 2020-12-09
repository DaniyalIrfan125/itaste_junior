package com.techbayportal.itaste.ui.fragments.loginfragment

import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSecondBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_loginfragment.*
import java.util.*

@AndroidEntryPoint
class LoginFragment : BaseFragment<LayoutSecondBinding, LoginViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_loginfragment
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onLoginClicked.observe(this, androidx.lifecycle.Observer {

        })

        mViewModel.onForgotPasswordClicked.observe(this, androidx.lifecycle.Observer {

            Navigation.findNavController(ed_enterUserName)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

}