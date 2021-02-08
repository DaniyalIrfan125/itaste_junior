package com.techbayportal.itaste.ui.fragments.myprofilefragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentMyProfileBinding
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.ui.fragments.reportbugdialogfragment.ReportBugDialogFragment
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        super.onViewCreated(view, savedInstanceState)
        mView = view
        subscribeToObserveDarkModeDataStore()
        subscribeToObserveLanguage()
        initializing()


    }

    private fun initializing() {
        if (loginSession != null) {
//Set these as live data and then observe

            tv_userName.text = loginSession.data.first + " " + loginSession.data.last

            Picasso.get().load(loginSession.data.profile_pic).fit().centerCrop()
                .into(siv_userImage, object :
                    Callback {
                    override fun onSuccess() {
                        if(sk_myProfile != null){
                            sk_myProfile.visibility = View.GONE
                        }

                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image).into(siv_userImage)
                        if(sk_myProfile != null){
                            sk_myProfile.visibility = View.GONE
                        }

                    }
                })


            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.VENDOR, true)) {
                ll_switchToPremium.visibility = View.GONE
                tv_description_switch_to_premium.visibility = View.GONE

            }

        }

    }

    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel.reportBugButtonsClicked.observe(this, Observer {
            if (it == AppConstants.ReportBugDialog.SUBMIT) {
                if (it != -1) {
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

                    //setting login session to null
                    LoginSession.getInstance().setLoginResponse(null)
                    //clearing user object after logout
                    GlobalScope.launch {
                        dataStoreProvider.clearUserObj()
                    }
                    navigateToLoginScreen()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
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

                    DialogClass.successDialog(
                        requireContext(),
                        getString(R.string.bug_report_submitted),
                        baseDarkMode
                    )
                    //Closing report a bug dialog fragment
                    Navigation.findNavController(mView).popBackStack()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
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

                    Navigation.findNavController(tv_editProfile)
                        .navigate(R.id.action_myProfileFragment_to_userProfileFragment)

                } else if (loginSession.data.role.equals(AppConstants.UserTypeKeys.VENDOR, true)) {
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

        mViewModel.onSwitchToPremiumClicked.observe(this, Observer {
            GlobalScope.launch {
                dataStoreProvider.switchToPremium(true)
            }
            Navigation.findNavController(tv_blockedAccounts)
                .navigate(R.id.action_myProfileFragment_to_signUpVendorFragment2)
            //  navigateToMainNavForVendorSignUp()
        })
    }


    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun navigateToMainNavForVendorSignUp() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.putExtra("OPEN_SIGNUP_SCREEN", "sign_up_vendor")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    private fun subscribeToObserveLanguage() {
        //observing data from data store and showing
        dataStoreProvider.languageFlow.asLiveData().observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it == "ar") {

                    mycart_fwd_arrow.rotation = 180F
                    change_password_fwd_arrow.rotation = 180F
                    saved_post_fwd_arrow.rotation = 180F
                    report_bug_fwd_arrow.rotation = 180F
                    delete_account_fwd_arrow.rotation = 180F
                    blocked_account_fwd_arrow.rotation = 180F
                    logout_fwd_arrow.rotation = 180F
                }
            }
        })

    }


}