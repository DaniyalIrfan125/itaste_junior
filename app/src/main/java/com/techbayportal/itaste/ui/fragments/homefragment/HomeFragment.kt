package com.techbayportal.itaste.ui.fragments.homefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_home_recyclerview.*
import kotlinx.android.synthetic.main.layout_homefragment.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<LayoutHomefragmentBinding, HomeViewModel>() , HomeRvClickListener {

    override val layoutId: Int
        get() = R.layout.layout_homefragment
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var homerRecyclerAdpater : HomeRecyclerAdapter

    lateinit var dataStoreProvider: DataStoreProvider

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
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToObserveDarkActivation()
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
                Navigation.findNavController(img_dots)
                    .navigate(R.id.action_homeFragment_to_profileFragment)
            }
            AppConstants.RecyclerViewKeys.HOME_RV_CHILD -> {
                Toast.makeText(context, "Home Child", Toast.LENGTH_SHORT).show()
            }

            AppConstants.RecyclerViewKeys.HOME_RV_IMG_DOTS -> {
                Navigation.findNavController(iv_home_configuration)
                    .navigate(R.id.action_homeFragment_to_homeItemBottomSheetFragment)

            }
        }
    }

    override fun onChildItemClick(position: Int) {
        Navigation.findNavController(iv_icon)
            .navigate(R.id.action_homeFragment_to_postDetailFragment)
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
          //  switch_darkMode.isChecked = it
            if (it != null) {
                if(it == true){
                    iv_icon.setImageDrawable(resources.getDrawable(R.drawable.app_icon_dark))
                }
            }
        })

    }
}