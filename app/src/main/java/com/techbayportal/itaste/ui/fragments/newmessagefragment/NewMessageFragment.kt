package com.techbayportal.itaste.ui.fragments.newmessagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentMessageNewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_message_new.*

@AndroidEntryPoint
class NewMessageFragment : BaseFragment<FragmentMessageNewBinding, NewMessageViewModel>() {

    override val layoutId: Int
        get() = R.layout.fragment_message_new
    override val viewModel: Class<NewMessageViewModel>
        get() = NewMessageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onCancelClicked.observe(this, Observer {
            Navigation.findNavController(ll_cancel).popBackStack()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


}