package com.techbayportal.itaste.ui.activities.signupactivity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseActivity
import com.techbayportal.itaste.databinding.ActivitySignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding, SignupViewModel>() {

    override val viewModel: Class<SignupViewModel>
        get() = SignupViewModel::class.java
    override val layoutId: Int
        get() = R.layout.activity_signup
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        var fragmentName = intent.getStringExtra("OPEN_SIGNUP_SCREEN")


        if (fragmentName == "sign_up") {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val navInflater = navController.navInflater
            val graph = navInflater.inflate(R.navigation.signup_graph)
            graph.startDestination = R.id.selectAccountTypeFragment2
            navController.graph = graph

        }else if(fragmentName == "sign_up_vendor"){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val navInflater = navController.navInflater
            val graph = navInflater.inflate(R.navigation.signup_graph)
            graph.startDestination = R.id.signUpVendorFragment
            navController.graph = graph
        }
    }


}