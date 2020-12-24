package com.techbayportal.itaste.ui.fragments.userprofilefragment

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_profile.*

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding, UserProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_profile
    override val viewModel: Class<UserProfileViewModel>
        get() = UserProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onSaveChangesButtonClicked.observe(this, Observer {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
        })
    }


}