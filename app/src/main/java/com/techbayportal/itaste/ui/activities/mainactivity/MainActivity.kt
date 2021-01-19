package com.techbayportal.itaste.ui.activities.mainactivity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseActivity
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.databinding.ActivityMainBinding
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: Class<MainViewModel>
        get() = MainViewModel::class.java

    override val layoutId: Int
        get() = R.layout.activity_main

    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var progress_bar: ProgressBar

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialising()
    }

    private fun initialising() {
        progress_bar = findViewById(R.id.progress_bar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottom_navigation, navController)



        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.main_navigation_graph)
        graph.startDestination = R.id.homeFragment
        navController.graph = graph
        bottom_navigation.setupWithNavController(navHostFragment.navController)*/


        relative_addButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "clicked add button", Toast.LENGTH_SHORT).show()
        })

        if (loginSession != null) {
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.USER, true)) {
                // relative_addButton.background = null
                relative_addButton.isVisible = false


            } else {

            }
        }
    }

    open fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    open fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }


    fun onAddButtonVisibility(isShow: Boolean) {
        if (isShow)
            relative_addButton.visibility = View.VISIBLE
        else
            relative_addButton.visibility = View.GONE
    }

}