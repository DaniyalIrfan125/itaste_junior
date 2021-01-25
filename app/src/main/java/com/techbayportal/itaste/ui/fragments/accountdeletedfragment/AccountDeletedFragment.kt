package com.techbayportal.itaste.ui.fragments.accountdeletedfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentAccountDeletedBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account_deleted.*

@AndroidEntryPoint
class AccountDeletedFragment : BaseFragment<FragmentAccountDeletedBinding, AccountDeletedViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_account_deleted
    override val viewModel: Class<AccountDeletedViewModel>
        get() = AccountDeletedViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onDoneAndExitButtonClicked.observe(this, Observer {
            /*Navigation.findNavController(btn_doneAndExit)
                .navigate(R.id.action_deleteAccountFragment_to_accountDeletedFragment)*/
            //Toast.makeText(context, "go to login", Toast.LENGTH_SHORT).show()

            navigateToLoginScreen()
        })

        mViewModel.onCreateNewAccountButtonClicked.observe(this, Observer {
            val intent = Intent (activity, SignupActivity::class.java)
            intent.putExtra("OPEN_SIGNUP_SCREEN", "sign_up")
            activity?.startActivity(intent)
            Toast.makeText(context, "go to signup", Toast.LENGTH_SHORT).show()
        })

        mViewModel.onBackToLoginButtonClicked.observe(this, Observer {
            navigateToLoginScreen()
        })
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }



}