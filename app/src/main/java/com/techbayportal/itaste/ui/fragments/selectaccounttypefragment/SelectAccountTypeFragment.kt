package com.techbayportal.itaste.ui.fragments.selectaccounttypefragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
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

    @SuppressLint("ResourceAsColor")
    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onExploreFoodClicked.observe(this, androidx.lifecycle.Observer {
            rl_explore_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_showcase_food.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_exploreFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            iv_showFood.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))
                //rl_explore_food.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))


        })

        mViewModel.onShowcaseFoodClicked.observe(this, androidx.lifecycle.Observer {
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
}