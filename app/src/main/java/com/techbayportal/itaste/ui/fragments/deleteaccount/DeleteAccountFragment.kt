package com.techbayportal.itaste.ui.fragments.deleteaccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentDeleteAccountBinding


class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding, DeleteAccountViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_delete_account
    override val viewModel: Class<DeleteAccountViewModel>
        get() = DeleteAccountViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}