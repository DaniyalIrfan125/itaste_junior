package com.techbayportal.itaste.ui.fragments.selectaccounttypefragment

import android.content.Intent
import android.widget.Toast
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutAccounttypefragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_accounttypefragment.*

@AndroidEntryPoint
class SelectAccountTypeFragment : BaseFragment<LayoutAccounttypefragmentBinding,SelectAccountTypeViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_accounttypefragment
    override val viewModel: Class<SelectAccountTypeViewModel>
        get() = SelectAccountTypeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onExploreFoodClicked.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(context, "Explore Food", Toast.LENGTH_SHORT).show()


        })

        mViewModel.onShowcaseFoodClicked.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(context, "Showcase Food", Toast.LENGTH_SHORT).show()

        })

        mViewModel.onNextButtonClicked.observe(this, androidx.lifecycle.Observer {
            Navigation.findNavController(btn_next)
                .navigate(R.id.action_selectAccountTypeFragment2_to_signUpFragment)
        })
    }
}