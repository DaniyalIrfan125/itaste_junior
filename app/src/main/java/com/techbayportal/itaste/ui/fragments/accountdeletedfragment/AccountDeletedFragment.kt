package com.techbayportal.itaste.ui.fragments.accountdeletedfragment

import android.os.Bundle
import android.view.View
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentAccountDeletedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDeletedFragment : BaseFragment<FragmentAccountDeletedBinding, AccountDeletedViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_account_deleted
    override val viewModel: Class<AccountDeletedViewModel>
        get() = AccountDeletedViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}