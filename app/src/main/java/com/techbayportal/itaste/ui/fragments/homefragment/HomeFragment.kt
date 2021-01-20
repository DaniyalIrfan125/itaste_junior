package com.techbayportal.itaste.ui.fragments.homefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment.HomeConfigurationBottomSheetFragment
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
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

    lateinit var mView :View
    val bottomSheet = HomeConfigurationBottomSheetFragment()

    val loginSession = LoginSession.getInstance().getLoginResponse()
    lateinit var homerRecyclerAdpater : HomeRecyclerAdapter

    lateinit var dataStoreProvider: DataStoreProvider
    val countriesList = ArrayList<GetAllCountriesData>()



    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onHomeConfigButtonClicked.observe(this, Observer {
//            Navigation.findNavController(iv_home_configuration)
//                .navigate(R.id.action_homeFragment_to_homeConfigurationBottomSheetFragment)
            //
            bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheet")
            mViewModel.hitGetAllCountriesForHome()
        })

        mViewModel.tempClicked.observe(this, Observer {
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populatingDataForHome()
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToObserveDarkActivation()
        sharedViewModel.test = true
        mView = view
       // mViewModel.loginSession!!.data.username
        initilizing()
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

    private fun initilizing() {
        if (loginSession != null) {
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.USER, true)) {
                // bottom_navigation.menu.findItem(R.id.chatInboxFragment).isVisible = false

            } else {

            }
        }


    }

    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel._homeConfigBottomSheetClickId.observe(this, Observer {
            if(it == AppConstants.HomeConfigBottomSheet.SETTINGS){
                if (it != -1) {
                    //call api to select country
                    Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                   // bottomSheet.dismiss()
                    if(this::mView.isInitialized){
                        Navigation.findNavController(mView).navigate(R.id.action_homeFragment_to_settingsFragment)
                    }


                    sharedViewModel._homeConfigBottomSheetClickId.value = -1
                }
            }
            else if(it == AppConstants.HomeConfigBottomSheet.UPDATE_LOCATION){
                if (it != -1) {
                    //call api to select country
                  //  Toast.makeText(context, "Update Loc: ${sharedViewModel.userUpdatedCountryId}", Toast.LENGTH_SHORT).show()
                    mViewModel.hitUpdateUserLocationFromHome(sharedViewModel.userUpdatedCountryId)

                    sharedViewModel._homeConfigBottomSheetClickId.value = -1
                }
            }
            /*else if(it == AppConstants.HomeConfigBottomSheet.TURN_OFF_NOTIFICATION){
                    if (it != -1) {
                        //call api to select country
                        Toast.makeText(context, "Notify 2", Toast.LENGTH_SHORT).show()

                        sharedViewModel._isSelectedCountryId?.value = -1
                    }
                }*/
            /*else if(it == AppConstants.HomeConfigBottomSheet.LOGOUT){
                    if (it != -1) {
                        //call api to select country
                        Toast.makeText(context, "Logout 2", Toast.LENGTH_SHORT).show()

                        sharedViewModel._isSelectedCountryId?.value = -1
                    }
                }*/

        })
    }


    //Home Screen RecyclearViews Items Clicks
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
    override fun subscribeToNetworkLiveData() {

        mViewModel.getCountriesResponse.observe(this, Observer {
            //  Snackbar.make(requireView(), "Country", Snackbar.LENGTH_SHORT).show()
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    countriesList.addAll(it.data!!.data)
                    sharedViewModel._countriesList.value = it.data

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.updateUserLocationResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                   // Snackbar.make(requireView(), "Country Updated" , Snackbar.LENGTH_SHORT).show()
                    //hit again to refresh data
                    mViewModel.hitGetAllCountriesForHome()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })


    }

    override fun onChildItemClick(position: Int) {
        Navigation.findNavController(iv_icon)
            .navigate(R.id.action_homeFragment_to_postDetailFragment)
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(viewLifecycleOwner, Observer {
          //  switch_darkMode.isChecked = it
            if (it != null) {
                if(it == true){
                    iv_icon.setImageDrawable(resources.getDrawable(R.drawable.app_icon_dark))
                }
            }
        })

    }
}