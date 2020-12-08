package com.techbayportal.itaste.ui.activities.signupactivity

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseActivity
import com.techbayportal.itaste.databinding.ActivitySignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    override val viewModel: Class<SignupViewModel>
        get() = SignupViewModel::class.java
    override val layoutId: Int
        get() = R.layout.activity_signup
    override val bindingVariable: Int
        get() = BR.viewModel

}