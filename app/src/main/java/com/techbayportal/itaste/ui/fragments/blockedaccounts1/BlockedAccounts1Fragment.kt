package com.techbayportal.itaste.ui.fragments.blockedaccounts1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentBlokedAccounts1Binding
import com.techbayportal.itaste.ui.fragments.blockedaccounts1.adapter.BlockedAccounts1Adapter
import com.techbayportal.itaste.ui.fragments.blockedaccounts1.itemclicklistener.BlockedAccounts1RvClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BlockedAccounts1Fragment : BaseFragment<FragmentBlokedAccounts1Binding, BlockedAccounts1ViewModel>(), BlockedAccounts1RvClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_bloked_accounts1
    override val viewModel: Class<BlockedAccounts1ViewModel>
        get() = BlockedAccounts1ViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvBlockedAccounts.adapter = BlockedAccounts1Adapter(this)
    }


    override fun onItemClickListener() {
        Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()

    }


}