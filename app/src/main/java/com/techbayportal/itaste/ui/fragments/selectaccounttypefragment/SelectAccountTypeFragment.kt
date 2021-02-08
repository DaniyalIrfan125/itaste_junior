package com.techbayportal.itaste.ui.fragments.selectaccounttypefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.databinding.LayoutAccounttypefragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_accounttypefragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectAccountTypeFragment : BaseFragment<LayoutAccounttypefragmentBinding,SelectAccountTypeViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_accounttypefragment
    override val viewModel: Class<SelectAccountTypeViewModel>
        get() = SelectAccountTypeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserTypeSelected()
    }

    override fun onResume() {
        super.onResume()
        //if user click back so he can see his last saved user type
    }

    @SuppressLint("ResourceAsColor")
    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onExploreFoodClicked.observe(this, androidx.lifecycle.Observer {

            sharedViewModel.userType = AppConstants.UserTypeKeys.USER

            rl_explore_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_showcase_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_exploreFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            iv_showFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))


        })

        mViewModel.onShowcaseFoodClicked.observe(this, androidx.lifecycle.Observer {
            GlobalScope.launch {
                dataStoreProvider.switchToPremium(false)
            }

            sharedViewModel.userType = AppConstants.UserTypeKeys.VENDOR

            rl_showcase_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_explore_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_showFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            iv_exploreFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))

        })

        mViewModel.onNextButtonClicked.observe(this, androidx.lifecycle.Observer {
            Navigation.findNavController(btn_next)
                .navigate(R.id.action_selectAccountTypeFragment2_to_signUpFragment)
        })
    }

    //observing User type vendor or user
    private fun checkUserTypeSelected() {
        GlobalScope.launch {
            dataStoreProvider.switchToPremium(false)
        }
        if (sharedViewModel.userType == AppConstants.UserTypeKeys.USER) {
            rl_explore_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_showcase_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_exploreFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            iv_showFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))


        } else if(sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR){
            rl_showcase_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_explore_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_showFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            iv_exploreFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))
        }

    }
}