package com.techbayportal.itaste.ui.fragments.loginfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
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


      //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onLoginClicked.observe(this, androidx.lifecycle.Observer {
            /*Navigation.findNavController(ed_enterUserName)
                .navigate(R.id.action_loginFragment_to_main_navigation_graph)*/

            val intent = Intent (activity, MainActivity::class.java)
            //intent.clearStack()
            activity?.startActivity(intent)


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