package com.techbayportal.itaste.ui.fragments.myprofilefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.databinding.FragmentMyProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile.img_back
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding, MyProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_my_profile
    override val viewModel: Class<MyProfileViewModel>
        get() = MyProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserveDarkModeDataStore()
        subscribeToObserveLanguage()



        switch_darkMode?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                GlobalScope.launch {
                    dataStoreProvider.storeDarkMode(true)
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                GlobalScope.launch {
                    dataStoreProvider.storeDarkMode(false)
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun subscribeToObserveDarkModeDataStore() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
            switch_darkMode.isChecked = it
        })

    }




    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onEditProfileClicked.observe(this, Observer {
            Navigation.findNavController(tv_editProfile)
                .navigate(R.id.action_myProfileFragment_to_userProfileFragment)
        })

        mViewModel.onMyCartClicked.observe(this, Observer {
            Navigation.findNavController(tv_myCart)
                .navigate(R.id.action_myProfileFragment_to_cartFragment)
        })

        mViewModel.onChangePasswordClicked.observe(this, Observer {
            Navigation.findNavController(tv_changePassword)
                .navigate(R.id.action_myProfileFragment_to_changeExistingPasswordFragment)
        })

        mViewModel.onSavedPostsClicked.observe(this, Observer {
            Navigation.findNavController(tv_savedPosts)
                .navigate(R.id.action_myProfileFragment_to_savedPostsFragment)
        })

        mViewModel.onReportBugClicked.observe(this, Observer {
            Navigation.findNavController(tv_reportBug)
                .navigate(R.id.action_myProfileFragment_to_reportBugDialogFragment)
        })

        mViewModel.onDeleteAccountClicked.observe(this, Observer {
            Navigation.findNavController(tv_deleteAccount)
                .navigate(R.id.action_myProfileFragment_to_deleteAccountFragment)
        })

        mViewModel.onBlockedAccountClicked.observe(this, Observer {
            Navigation.findNavController(tv_blockedAccounts)
                .navigate(R.id.action_myProfileFragment_to_blockedAccountsFragment)
        })

        mViewModel.onLogoutClicked.observe(this, Observer {
            Navigation.findNavController(tv_logout)
                .navigate(R.id.action_myProfileFragment_to_welcomefragment)
            //Toast.makeText(context, "Move To Welcome Fragment", Toast.LENGTH_SHORT).show()
        })




    }
    private fun subscribeToObserveLanguage() {
        //observing data from data store and showing
        dataStoreProvider.languageFlow.asLiveData().observe(this, Observer {

            if (it != null) {
                if (it == "ar") {
                    // tv_language_name.text = getString(R.string.arabic)

                    mycart_fwd_arrow.rotation = 180F
                    change_password_fwd_arrow.rotation = 180F
                    saved_post_fwd_arrow.rotation = 180F
                    report_bug_fwd_arrow.rotation = 180F
                    delete_account_fwd_arrow.rotation = 180F
                    blocked_account_fwd_arrow.rotation = 180F
                    logout_fwd_arrow.rotation = 180F

                  //  iv_cart.rotation = 180F
                  //  iv_moon.rotation = 180F


                } else {
                    // tv_language_name.text = getString(R.string.english)
                }
            } else {
                // tv_language_name.text = getString(R.string.english)
            }
        })

    }
    


}