package com.techbayportal.itaste.ui.activities.mainactivity

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseActivity
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.databinding.ActivityMainBinding
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: Class<MainViewModel>
        get() = MainViewModel::class.java

    override val layoutId: Int
        get() = R.layout.activity_main

    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var progress_bar: ProgressBar
    //lateinit var dataStoreProviderBase: DataStoreProvider

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //dataStoreProviderBase = DataStoreProvider(this)
        initialising()

        //If user payment is not submitted this will take him to Profile Fragment where he can update his payment
        val intent = intent
        val fragmentName = intent.getStringExtra("PROFILE_FRAGMENT")

        if(loginSession != null){
            dataStoreProvider.switchToPremiumFlow.asLiveData().observe(this, Observer { switchToPremium ->
                if (switchToPremium == true) {
                    if (fragmentName == "profile" || !loginSession.data.is_payment_update) {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                        val navController = navHostFragment.navController
                        val navInflater = navController.navInflater
                        val graph = navInflater.inflate(R.navigation.main_navigation_graph)
                        graph.startDestination = R.id.profileFragment
                        navController.graph = graph

                    }
                }
            })
        }
    }

    private fun initialising() {
        progress_bar = findViewById(R.id.progress_bar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottom_navigation, navController)


        //Add post button for vendor
        relative_addButton.setOnClickListener {
            //ask for permissions
            Dexter.withContext(this@MainActivity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                    ,Manifest.permission.CAMERA)
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                Timber.d("PER: OK")
                                navController.navigate(R.id.selectPostFragment)
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Remember to invoke this method when the custom rationale is closed
                        // or just by default if you don't want to use any custom rationale.
                        token?.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Timber.d("PER: ${it.name}")
                }
                .check()


        }

        if (loginSession != null) {
            //removing add Post button if user is logged in
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.USER, true)) {
                relative_addButton.isVisible = false
            }

        }

        bottom_navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (navController.currentDestination != null) {
                if (item.itemId == R.id.homeFragment) {
                    if (navController.currentDestination!!.label != "HomeFragment") {
                        navController.popBackStack(R.id.homeFragment, false)
                    }
                } else if (item.itemId == R.id.notificationFragment) {
                    if (navController.currentDestination!!.label != "NotificationFragment") {
                        if (!navController.popBackStack(R.id.notificationFragment, false)) {
                            navController.navigate(R.id.notificationFragment)
                        }
                    }
                } else if (item.itemId == R.id.selectPostFragment) {
                    if (navController.currentDestination!!.label != "SelectPostFragment") {
                        if (!navController.popBackStack(R.id.selectPostFragment, false)) {
                            navController.navigate(R.id.selectPostFragment)
                        }
                    }
                }  else if (item.itemId == R.id.searchFragment) {
                    if (navController.currentDestination!!.label != "SearchFragment") {
                        if (!navController.popBackStack(R.id.searchFragment, false)) {
                            navController.navigate(R.id.searchFragment)
                        }
                    }
                }
                else if (item.itemId == R.id.myProfileFragment) {
                    if (navController.currentDestination!!.label != "MyProfileFragment") {
                        if (!navController.popBackStack(R.id.myProfileFragment, false)) {
                            navController.navigate(R.id.myProfileFragment)
                        }
                    }
                } else if (item.itemId == R.id.directMessagesFragment) {
                    if (navController.currentDestination!!.label != "DirectMessagesFragment") {
                        if (!navController.popBackStack(R.id.directMessagesFragment, false)) {
                            navController.navigate(R.id.directMessagesFragment)
                        }
                    }
                }
            }
            true
        })
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