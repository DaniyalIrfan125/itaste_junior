package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutChangepasswordfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<LayoutChangepasswordfragmentBinding, ChangePasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changepasswordfragment
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel




    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_changePassword)
                .popBackStack()
        })
    }



    }