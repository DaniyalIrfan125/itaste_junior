package com.techbayportal.itaste.ui.fragments.newmessagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMessageNewBinding

class NewMessageFragment : BaseFragment<FragmentMessageNewBinding, NewMessageViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_message_new
    override val viewModel: Class<NewMessageViewModel>
        get() = NewMessageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}