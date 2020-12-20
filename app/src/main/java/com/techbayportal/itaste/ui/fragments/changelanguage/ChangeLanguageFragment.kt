package com.techbayportal.itaste.ui.fragments.changelanguage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentChangeLanguageBinding

class ChangeLanguageFragment : BaseFragment<FragmentChangeLanguageBinding, ChangeLangaugeViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_change_language
    override val viewModel: Class<ChangeLangaugeViewModel>
        get() = ChangeLangaugeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

}