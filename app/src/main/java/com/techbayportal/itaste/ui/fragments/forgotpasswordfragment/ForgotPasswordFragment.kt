package com.techbayportal.itaste.ui.fragments.forgotpasswordfragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutForgotpasswordfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.*
import kotlinx.android.synthetic.main.layout_forgotpasswordfragment.btn_next
import kotlinx.android.synthetic.main.layout_loginfragment.*
import kotlinx.android.synthetic.main.layout_otpverificationfragment.*

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<LayoutForgotpasswordfragmentBinding,ForgotPasswordFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_forgotpasswordfragment
    override val viewModel: Class<ForgotPasswordFragmentViewModel>
        get() = ForgotPasswordFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fieldTextWatcher()
    }

    private fun fieldTextWatcher() {
        ed_forgotPassword.doOnTextChanged { text, start, before, count ->
            tv_errorForgotPassword.visibility = View.GONE
            ed_forgotPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }

    fun validationsCheck(){
        if (!TextUtils.isEmpty(ed_forgotPassword.text)) {
            Navigation.findNavController(btn_next).navigate(R.id.action_forgotPasswordFragment_to_otpverificationFragment)
        } else {
            tv_errorForgotPassword.visibility = View.VISIBLE
            ed_forgotPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_errorForgotPassword.text = "Please write Username!"
        }

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {

            validationsCheck()

        })

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_backForgot)
                .popBackStack()
        })
    }
}