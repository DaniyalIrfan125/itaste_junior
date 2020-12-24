package com.techbayportal.itaste.ui.fragments.cartfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutCartfragmentBinding
import com.techbayportal.itaste.ui.fragments.cartfragment.adapter.CartsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_cartfragment.*
import kotlinx.android.synthetic.main.layout_cartfragment.img_back



@AndroidEntryPoint
class CartFragment : BaseFragment<LayoutCartfragmentBinding,CartViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_cartfragment
    override val viewModel: Class<CartViewModel>
        get() = CartViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_cart.adapter = CartsAdapter(
            listOf<Int>(
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first
            ),
            requireContext()
        )
        recycler_cart.layoutManager = LinearLayoutManager(context)
    }
}