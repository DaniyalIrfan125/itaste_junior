package com.techbayportal.itaste.ui.fragments.deleteaccountfragment

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentDeleteAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_delete_account.*

@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding, DeleteAccountViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_delete_account
    override val viewModel: Class<DeleteAccountViewModel>
        get() = DeleteAccountViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onConfirmButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_confirm)
                .navigate(R.id.action_deleteAccountFragment_to_accountDeletedFragment)
        })

        mViewModel.onCancelButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


}