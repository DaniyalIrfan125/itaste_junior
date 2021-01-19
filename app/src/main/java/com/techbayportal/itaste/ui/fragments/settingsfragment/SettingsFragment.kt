package com.techbayportal.itaste.ui.fragments.settingsfragment


import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: Class<SettingsFragmentViewModel>
        get() = SettingsFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_back.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(img_back).popBackStack()
        })
    }
}