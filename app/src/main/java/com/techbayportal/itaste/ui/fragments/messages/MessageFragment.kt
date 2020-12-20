package com.techbayportal.itaste.ui.fragments.messages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMessageBinding
import com.techbayportal.itaste.ui.fragments.messages.adapter.UserMessagesItemAdapter
import com.techbayportal.itaste.ui.fragments.messages.itemClickListener.UserMessageRvItemClickListener

class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>(), UserMessageRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_message
    override val viewModel: Class<MessageViewModel>
        get() = MessageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvUsersForChat.adapter = UserMessagesItemAdapter(this)
    }

    override fun onItemClickListener() {
        Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()

    }



}