package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.databinding.FragmentBlokedAccountsBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter.BlockedAccountsAdapter
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccountsRvClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bloked_accounts.*
import kotlinx.android.synthetic.main.fragment_bloked_accounts.img_back

@AndroidEntryPoint
class BlockedAccountsFragment : BaseFragment<FragmentBlokedAccountsBinding, BlockedAccountsViewModel>(), BlockedAccountsRvClickListener {



    override val layoutId: Int
        get() = R.layout.fragment_bloked_accounts
    override val viewModel: Class<BlockedAccountsViewModel>
        get() = BlockedAccountsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var blockedAccountsAdapter: BlockedAccountsAdapter

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blockedAccountsAdapter = BlockedAccountsAdapter(requireContext())
        rvBlockedAccounts.adapter = blockedAccountsAdapter
        rvBlockedAccounts.layoutManager = LinearLayoutManager(context)
        blockedAccountsAdapter.setOnEntryClickListener(this)
    }


    override fun onItemClickListener(type : String) {
        when(type) {
            AppConstants.RecyclerViewKeys.BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON -> {
                Toast.makeText(context, "Unblock", Toast.LENGTH_SHORT).show()
            }
        }
    }

}