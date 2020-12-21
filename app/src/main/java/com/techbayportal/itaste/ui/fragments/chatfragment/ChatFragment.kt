package com.techbayportal.itaste.ui.fragments.chatfragment

import android.os.Bundle
import android.view.View
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentChatBinding
import com.techbayportal.itaste.ui.fragments.chatfragment.adapter.ChatAdapter
import com.techbayportal.itaste.ui.fragments.chatfragment.itemClickListener.ChatRvItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatFragmentViewModel>() ,ChatRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_chat
    override val viewModel: Class<ChatFragmentViewModel>
        get() = ChatFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvMessages.adapter = ChatAdapter(requireContext())
    }

    override fun onItemClickListener() {
       // Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()
    }

}