package com.techbayportal.itaste.ui.fragments.homefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_home_recyclerview.*
import kotlinx.android.synthetic.main.layout_homefragment.*
import kotlinx.android.synthetic.main.layout_homefragment.view.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<LayoutHomefragmentBinding, HomeViewModel>() , HomeRvClickListener {

    override val layoutId: Int
        get() = R.layout.layout_homefragment
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var homerRecyclerAdpater : HomeRecyclerAdapter
    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onHomeConfigButtonClicked.observe(this, Observer {
            Navigation.findNavController(iv_home_configuration)
                .navigate(R.id.action_homeFragment_to_homeConfigurationBottomSheetFragment)
        })

        mViewModel.tempClicked.observe(this, Observer {

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populatingDataForHome()
    }

    private fun populatingDataForHome() {
        homerRecyclerAdpater = HomeRecyclerAdapter(
            listOf<Int>(
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first
            ),
            requireContext()
        )
        recycler_home.layoutManager = LinearLayoutManager(context)
        recycler_home.adapter = homerRecyclerAdpater
        homerRecyclerAdpater.setOnEntryClickListener(this)

    }

    override fun onItemClickListener(type : String) {
        when(type){
            AppConstants.RecyclerViewKeys.HOME_RV -> {
                Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show()
            }
            AppConstants.RecyclerViewKeys.HOME_RV_CHILD -> {
                Toast.makeText(context, "Home Child", Toast.LENGTH_SHORT).show()
            }

            AppConstants.RecyclerViewKeys.HOME_RV_IMG_DOTS -> {
                Navigation.findNavController(img_dots)
                    .navigate(R.id.action_homeFragment_to_profileFragment)
            }
        }
    }

    override fun onChildItemClick(position: Int) {
        Navigation.findNavController(iv_icon)
            .navigate(R.id.action_homeFragment_to_postDetailFragment)
    }
}