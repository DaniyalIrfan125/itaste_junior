package com.techbayportal.itaste.ui.fragments.message3

import android.os.Bundle
import android.view.View
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMessage3Binding
import com.techbayportal.itaste.ui.fragments.message3.adapter.ChatAdapter
import com.techbayportal.itaste.ui.fragments.message3.itemClickListener.ChatRvItemClickListener


class Message3Fragment : BaseFragment<FragmentMessage3Binding, ChatViewModel>() ,ChatRvItemClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_message3
    override val viewModel: Class<ChatViewModel>
        get() = ChatViewModel::class.java
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