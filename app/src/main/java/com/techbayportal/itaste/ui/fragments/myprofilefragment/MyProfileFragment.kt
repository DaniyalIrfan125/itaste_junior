package com.techbayportal.itaste.ui.fragments.myprofilefragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.LoginResponse
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentMyProfileBinding
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.ui.fragments.reportbugdialogfragment.ReportBugDialogFragment
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.layout_profilefragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding, MyProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_my_profile
    override val viewModel: Class<MyProfileViewModel>
        get() = MyProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    val loginSession = LoginSession.getInstance().getLoginResponse()
    lateinit var mView: View

    lateinit var dataStoreProvider: DataStoreProvider
    lateinit var reportBugDialogFragment: ReportBugDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        subscribeToObserveDarkModeDataStore()
        subscribeToObserveLanguage()
        initializing()



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

    private fun initializing(){
        if (loginSession != null){
            tv_userName.text = loginSession.data.first +" "+ loginSession.data.last

            Picasso.get().load(loginSession.data.profile_img).fit().centerCrop().into(siv_userImage , object :
                Callback {
                override fun onSuccess() {
                    sk_myProfile.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    Picasso.get().load(R.drawable.placeholder_image).into(siv_userImage)
                    sk_myProfile.visibility = View.GONE
                }
            })
        }

    }

    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel.reportBugButtonsClicked.observe(this, Observer {
            if(it == AppConstants.ReportBugDialog.SUBMIT){
                if(it != -1){
                   // Toast.makeText(requireContext(), "Submit Clicked", Toast.LENGTH_SHORT).show()

                    mViewModel.hitReportBugApi(sharedViewModel.bugReportMessage)
                }
                sharedViewModel.reportBugButtonsClicked.value = -1
            }
        })

    }

    private fun subscribeToObserveDarkModeDataStore() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(viewLifecycleOwner, Observer {
            switch_darkMode.isChecked = it
        })

    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.logoutResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    /*GlobalScope.launch {
                        dataStoreProvider.saveUserObj(null!!)
                    }*/

                    LoginSession.getInstance().setLoginResponse(null)
                    GlobalScope.launch {
                        dataStoreProvider.clearUserObj()
                    }
                    navigateToLoginScreen()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode )
                }
            }
        })

        mViewModel.getReportBugResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    Toast.makeText(requireContext(), "Report Submitted", Toast.LENGTH_SHORT).show()
                   // reportBugDialogFragment.dismiss()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode )
                }
            }
        })
    }




    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(mView).popBackStack()
        })

        mViewModel.onEditProfileClicked.observe(this, Observer {

            if (loginSession != null) {
                if (loginSession.data.role.equals(AppConstants.UserTypeKeys.USER, true)) {

                    Navigation.findNavController(tv_editProfile).navigate(R.id.action_myProfileFragment_to_userProfileFragment)
                    //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                } else {
                    Navigation.findNavController(tv_editProfile)
                        .navigate(R.id.action_myProfileFragment_to_vendorProfileFragment)
                }
            }

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

            mViewModel.hitLogout()


        })
    }



    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)


    }
    private fun subscribeToObserveLanguage() {
        //observing data from data store and showing
        dataStoreProvider.languageFlow.asLiveData().observe(viewLifecycleOwner, Observer {

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