package com.techbayportal.itaste.ui.fragments.settingsfragment


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentSettingsBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.img_back

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: Class<SettingsFragmentViewModel>
        get() = SettingsFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var mView :View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()
        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onContactUsClicked.observe(this, Observer {
            Navigation.findNavController(mView).navigate(R.id.action_settingsFragment_to_contactUsFragment)
        })
        }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

       //All Network Calls
    }
}