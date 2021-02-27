package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetAllBlockedUserData
import com.techbayportal.itaste.data.models.GetAllBlockedUserResponse
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.PostDetailData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentBlokedAccountsBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter.BlockedAccountsAdapter
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccountsRvClickListener
import com.techbayportal.itaste.ui.fragments.profilefragment.adapter.PostsRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_bloked_accounts.*
import kotlinx.android.synthetic.main.fragment_bloked_accounts.img_back
import kotlinx.android.synthetic.main.layout_profilefragment.*

@AndroidEntryPoint
class BlockedAccountsFragment : BaseFragment<FragmentBlokedAccountsBinding, BlockedAccountsViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_bloked_accounts
    override val viewModel: Class<BlockedAccountsViewModel>
        get() = BlockedAccountsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var mView: View
    lateinit var getAllBlockedUserData: GetAllBlockedUserData
    lateinit var model : GetAllBlockedUserResponse
    lateinit var blockedAccountsAdapter: BlockedAccountsAdapter

    private val blockedAccountsList = ArrayList<GetAllBlockedUserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
        mViewModel.hitGetAllBlockedAccountsApi()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()
        if (this::model.isInitialized) {
            setData(model)
        }
       /* blockedAccountsAdapter = BlockedAccountsAdapter(requireContext())
        rvBlockedAccounts.adapter = blockedAccountsAdapter
        rvBlockedAccounts.layoutManager = LinearLayoutManager(context)
        blockedAccountsAdapter.setOnEntryClickListener(this)*/
    }

    private fun initializing() {
        blockedAccountsAdapter = BlockedAccountsAdapter(blockedAccountsList, object : BlockedAccountsAdapter.ClickItemListener{
            override fun onClicked(getAllBlockedUserData: GetAllBlockedUserData) {
              //  Toast.makeText(requireContext(), "Account selected: ${getAllBlockedUserData.first_name}", Toast.LENGTH_SHORT).show()
                //Temperory 31 was send as id below
                mViewModel.hitBlockAccountApi(getAllBlockedUserData.id)
            }

        })
        rvBlockedAccounts.adapter = blockedAccountsAdapter
        rvBlockedAccounts.layoutManager =  LinearLayoutManager(context)
       // blockedAccountsAdapter.setOnEntryClickListener(this)

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getAllBlockedAccountsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    model = it.data!!
                    setData(model)

                    //Toast.makeText(requireContext(), "Fetched Blocked Accounts", Toast.LENGTH_SHORT).show()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.blockAccountResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    mViewModel.hitGetAllBlockedAccountsApi()
                    blockedAccountsAdapter.notifyDataSetChanged()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    private fun setData(model : GetAllBlockedUserResponse){
        if(!model.data.isNullOrEmpty()){
            blockedAccountsList.clear()
            blockedAccountsList.addAll(model.data)
            blockedAccountsAdapter.notifyDataSetChanged()
            rvBlockedAccounts.visibility = View.VISIBLE
        }else{
            ll_noBlockedAccounts.visibility = View.VISIBLE
            rvBlockedAccounts.visibility = View.GONE
        }

    }


    /*override fun onItemClickListener(type : String) {
        when(type) {
            AppConstants.RecyclerViewKeys.BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON -> {
                Toast.makeText(context, "Unblock 1", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

}