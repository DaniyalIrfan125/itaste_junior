package com.techbayportal.itaste.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.databinding.FragmentSettingsBinding


class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: Class<SettingsViewModel>
        get() = SettingsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}