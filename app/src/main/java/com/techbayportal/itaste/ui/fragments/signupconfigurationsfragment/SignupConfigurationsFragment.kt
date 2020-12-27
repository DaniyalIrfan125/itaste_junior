package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupconfigurationsfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_accounttypefragment.*
import kotlinx.android.synthetic.main.layout_signupconfigurationsfragment.*
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class SignupConfigurationsFragment :
    BaseFragment<LayoutSignupconfigurationsfragmentBinding, SignUpConfigurationsViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupconfigurationsfragment
    override val viewModel: Class<SignUpConfigurationsViewModel>
        get() = SignUpConfigurationsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show()
        })

        mViewModel.onDarkModeButtonClicked.observe(this, Observer {
            rl_dark_mode.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_light_mode.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_moon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            iv_sun.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))
        })

        mViewModel.onLightModeButtonClicked.observe(this, Observer {
            rl_light_mode.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_dark_mode.background = ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_sun.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            iv_moon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_image_color))
        })
    }
}