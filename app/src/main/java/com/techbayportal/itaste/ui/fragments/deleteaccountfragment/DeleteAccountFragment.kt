package com.techbayportal.itaste.ui.fragments.deleteaccountfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentDeleteAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding, DeleteAccountViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_delete_account
    override val viewModel: Class<DeleteAccountViewModel>
        get() = DeleteAccountViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}