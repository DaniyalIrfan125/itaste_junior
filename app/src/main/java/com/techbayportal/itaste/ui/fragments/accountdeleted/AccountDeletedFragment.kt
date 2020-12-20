package com.techbayportal.itaste.ui.fragments.accountdeleted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentAccountDeletedBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDeletedFragment : BaseFragment<FragmentAccountDeletedBinding, AccountDeletedViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_delete_account
    override val viewModel: Class<AccountDeletedViewModel>
        get() = AccountDeletedViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}