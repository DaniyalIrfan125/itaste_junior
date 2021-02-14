package com.techbayportal.itaste.ui.fragments.settingsfragment


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentSettingsBinding
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: Class<SettingsFragmentViewModel>
        get() = SettingsFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var mView: View

    val loginSession = LoginSession.getInstance().getLoginResponse()

    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()
        dataStoreProvider = DataStoreProvider(requireContext())

    }

    private fun initializing() {
        if (loginSession != null) {
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.VENDOR, true)) {
                ll_switch_to_premium.visibility = View.GONE
                //Check if user payment is done or not on first time move him to user profile and ask
                //for payment
                /*if(!loginSession.data.is_payment_update){
                    Navigation.findNavController(mView).navigate(R.id.action_homeFragment_to_profileFragment)
                }*/

            }else{
                ll_switch_to_premium.visibility = View.VISIBLE
            }
        }
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()
        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onAboutUsClicked.observe(this, Observer {
            sharedViewModel.isFromAboutUs = true
            sharedViewModel.isFromTAndC = false
            sharedViewModel.aboutUsWebViewTitle = getString(R.string.about_us)
            sharedViewModel.aboutUsWebViewURL = "https://itaste.live/about-us"
            Navigation.findNavController(mView)
                .navigate(R.id.action_settingsFragment_to_webViewFragment)
        })

        mViewModel.onChangeLanguageClicked.observe(this, Observer {
            // Navigation.findNavController(mView).navigate(R.id.action_settingsFragment_to_contactUsFragment)
        })

        mViewModel.onTermAndCondClicked.observe(this, Observer {
            sharedViewModel.isFromTAndC = true
            sharedViewModel.isFromAboutUs = false
            sharedViewModel.tAndCTitle = getString(R.string.terms_and_conditions)
            sharedViewModel.tAndCWebViewURL = "https://itaste.live/terms-condition"
            Navigation.findNavController(mView)
                .navigate(R.id.action_settingsFragment_to_webViewFragment)
        })

        mViewModel.onContactUsClicked.observe(this, Observer {
            Navigation.findNavController(mView)
                .navigate(R.id.action_settingsFragment_to_contactUsFragment)
        })

        mViewModel.onHelpAndFaqsClicked.observe(this, Observer {
            sharedViewModel.isFromHelpAndFaqs = true
            sharedViewModel.isFromTAndC = false
            sharedViewModel.isFromAboutUs = false
            sharedViewModel.helpAndFaqsTitle = getString(R.string.help_and_FAQs)
            sharedViewModel.helpAndFaqsWebViewURL = "https://google.com"
            Navigation.findNavController(mView)
                .navigate(R.id.action_settingsFragment_to_webViewFragment)
        })

        mViewModel.onAdsClicked.observe(this, Observer {
            // Navigation.findNavController(mView).navigate(R.id.action_settingsFragment_to_contactUsFragment)
        })

        mViewModel.onLogoutClicked.observe(this, Observer {
            mViewModel.hitLogout()
        })

    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        //All Network Calls

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
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }
}