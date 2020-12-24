package com.techbayportal.itaste.ui.fragments.welcomefragment

import android.content.Intent
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutWelcomefragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Welcomefragment : BaseFragment<LayoutWelcomefragmentBinding, WelcomeViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_welcomefragment
    override val viewModel: Class<WelcomeViewModel>
        get() = WelcomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onSignInClicked.observe(this, androidx.lifecycle.Observer {

            val intent = Intent(activity, SignupActivity::class.java)
            //intent.setFlagTaskOnHome()
            startActivity(intent)


        })
    }
}