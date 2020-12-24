package com.techbayportal.itaste.ui.fragments.loginfragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSecondBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_loginfragment.*


@AndroidEntryPoint
class LoginFragment : BaseFragment<LayoutSecondBinding, LoginViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_loginfragment
    override val viewModel: Class<LoginViewModel>
        get() = LoginViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fieldTextWatcher()
    }

    private fun fieldTextWatcher() {
        ed_enterUserName.doOnTextChanged { text, start, before, count ->
            tv_userNameError.visibility = View.GONE
            ed_enterUserName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
        ed_password.doOnTextChanged { text, start, before, count ->
            tv_passwordError.visibility = View.GONE
            ed_password.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }


    fun fieldValidationsCheck() {

        if (!TextUtils.isEmpty(ed_enterUserName.text)) {
            if (!TextUtils.isEmpty(ed_password.text)) {

                navigateToMainActivity()
            } else {
                tv_passwordError.visibility = View.VISIBLE
                ed_password.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                tv_passwordError.text = "Please write password!"
            }

        } else {
            tv_userNameError.visibility = View.VISIBLE
            ed_enterUserName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_userNameError.text = "Please write Username!"
        }


    }


    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onLoginClicked.observe(this, androidx.lifecycle.Observer {

            fieldValidationsCheck()

        })

        mViewModel.onForgotPasswordClicked.observe(this, androidx.lifecycle.Observer {

            Navigation.findNavController(ed_enterUserName)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)

        })

        mViewModel.onSignUpClicked.observe(this, androidx.lifecycle.Observer {
            Navigation.findNavController(ed_enterUserName)
                .navigate(R.id.action_loginFragment_to_selectAccountTypeFragment2)
        })
    }


    override fun onStop() {
        super.onStop()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    }

    override fun onStart() {
        super.onStart()

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

}