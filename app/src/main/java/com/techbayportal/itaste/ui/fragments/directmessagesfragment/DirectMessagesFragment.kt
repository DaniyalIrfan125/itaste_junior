package com.techbayportal.itaste.ui.fragments.directmessagesfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentDirectMessagesBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter.DirectMessagesAdapter
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.itemClickListener.DirectMessagesRvItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_direct_messages.*

@AndroidEntryPoint
class DirectMessagesFragment : BaseFragment<FragmentDirectMessagesBinding, DirectMessagesFragmentViewModel>(), DirectMessagesRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_direct_messages
    override val viewModel: Class<DirectMessagesFragmentViewModel>
        get() = DirectMessagesFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNewMessageIconClicked.observe(this, Observer {
            Navigation.findNavController(ll_new_message)
                .navigate(R.id.action_directMessagesFragment_to_newMessageFragment)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvUsersForChat.adapter = DirectMessagesAdapter(this)
    }

    override fun onItemClickListener() {
        Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()
    }



}