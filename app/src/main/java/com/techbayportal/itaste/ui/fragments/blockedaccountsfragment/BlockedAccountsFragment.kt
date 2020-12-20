package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentBlokedAccountsBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter.BlockedAccountsAdapter
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccounts1RvClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlockedAccountsFragment : BaseFragment<FragmentBlokedAccountsBinding, BlockedAccountsViewModel>(), BlockedAccounts1RvClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_bloked_accounts
    override val viewModel: Class<BlockedAccountsViewModel>
        get() = BlockedAccountsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvBlockedAccounts.adapter = BlockedAccountsAdapter(this)
    }


    override fun onItemClickListener() {
        Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()
    }

}