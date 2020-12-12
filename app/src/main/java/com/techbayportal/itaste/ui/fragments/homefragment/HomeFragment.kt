package com.techbayportal.itaste.ui.fragments.homefragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_homefragment.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<LayoutHomefragmentBinding, HomeViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_homefragment
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populatingDataForHome()
    }

    private fun populatingDataForHome() {
        recycler_home.adapter = HomeRecyclerAdapter(
            listOf<Int>(
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first
            ),
            requireContext()
        )
        recycler_home.layoutManager = LinearLayoutManager(context)

    }
}