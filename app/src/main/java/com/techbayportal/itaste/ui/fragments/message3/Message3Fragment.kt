package com.techbayportal.itaste.ui.fragments.message3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMessage3Binding
import com.techbayportal.itaste.ui.fragments.message3.adapter.ChatAdapter
import com.techbayportal.itaste.ui.fragments.message3.itemClickListener.ChatRvItemClickListener
import com.techbayportal.itaste.ui.fragments.messages.adapter.UserMessagesItemAdapter


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