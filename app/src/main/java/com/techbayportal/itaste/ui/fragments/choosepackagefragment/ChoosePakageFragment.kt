package com.techbayportal.itaste.ui.fragments.choosepackagefragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutChoosepackagefragmentBinding
import com.techbayportal.itaste.ui.fragments.choosepackagefragment.adapter.ChoosePakageAdapter
import kotlinx.android.synthetic.main.layout_choosepackagefragment.*

class ChoosePakageFragment : BaseFragment<LayoutChoosepackagefragmentBinding,ChoosePackageViewModel>(){
    override val layoutId: Int
        get() = R.layout.layout_choosepackagefragment
    override val viewModel: Class<ChoosePackageViewModel>
        get() = ChoosePackageViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recycler_choosepakage.adapter = ChoosePakageAdapter(
            listOf<Int>(
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first
            ),
            requireContext()
        )

        recycler_choosepakage.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}