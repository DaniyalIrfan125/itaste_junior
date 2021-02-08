package com.techbayportal.itaste.ui.fragments.homefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutHomefragmentBinding
import com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment.HomeConfigurationBottomSheetFragment
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener
import com.techbayportal.itaste.ui.fragments.homeitembottomsheetfragment.HomeItemBottomSheetFragment
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.android.synthetic.main.item_home_recyclerview.*
import kotlinx.android.synthetic.main.item_home_recyclerview.spinKit
import kotlinx.android.synthetic.main.layout_homefragment.*
import kotlinx.android.synthetic.main.layout_profilefragment.*
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : BaseFragment<LayoutHomefragmentBinding, HomeViewModel>(), HomeRvClickListener {

    override val layoutId: Int
        get() = R.layout.layout_homefragment
    override val viewModel: Class<HomeViewModel>
        get() = HomeViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var mView: View

    var thisVendorId: Int = 0

    val bottomSheet = HomeConfigurationBottomSheetFragment()
    private val homeItemBottomSheet = HomeItemBottomSheetFragment()

    val loginSession = LoginSession.getInstance().getLoginResponse()

    lateinit var dataStoreProvider: DataStoreProvider
    val countriesList = ArrayList<GetAllCountriesData>()

    lateinit var homerRecyclerAdpater: HomeRecyclerAdapter
    var vendorsDataList : ArrayList<GetHomeScreenData> = ArrayList()

    lateinit var getHomeScreenData : GetHomeScreenData
    var getHomeScreenData1 = ArrayList<GetHomeScreenData>()
    lateinit var getHomeScreenResponse: GetHomeScreenResponse

    lateinit var getHomeScreenPostsData: GetHomeScreenPostsData
    var vendorPostsList = ArrayList<GetHomeScreenPostsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initilizing()
        mViewModel.hitGetHomeScreenInfoApi()
        dataStoreProvider = DataStoreProvider(requireContext())
        subscribeToObserveDarkActivation()
        sharedViewModel.test = true


        if (this::getHomeScreenResponse.isInitialized) {
            setData(getHomeScreenResponse)
        }



    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onHomeConfigButtonClicked.observe(this, Observer {
//            Navigation.findNavController(iv_home_configuration)
//                .navigate(R.id.action_homeFragment_to_homeConfigurationBottomSheetFragment)
            //
            bottomSheet.show(requireActivity().supportFragmentManager, "bottomSheet")

            if (loginSession != null) {
                if (loginSession.data.role.equals(AppConstants.UserTypeKeys.USER, true)) {
                    // call api only when user is logged in
                    mViewModel.hitGetAllCountriesForHome()
                }
            }

        })

        mViewModel.tempClicked.observe(this, Observer {
        })
    }



       private fun initilizing() {
        homerRecyclerAdpater = HomeRecyclerAdapter(vendorsDataList, requireContext())
        recycler_home.layoutManager = LinearLayoutManager(context)
        recycler_home.adapter = homerRecyclerAdpater
        homerRecyclerAdpater.setOnEntryClickListener(this)


        if (loginSession != null) {
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.VENDOR, true)) {
                //Check if user payment is done or not on first time move him to user profile and ask
                //for payment
                /*if(!loginSession.data.is_payment_update){
                    Navigation.findNavController(mView).navigate(R.id.action_homeFragment_to_profileFragment)
                }*/

            }
        }


    }

    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel._homeConfigBottomSheetClickId.observe(this, Observer {
            if (it == AppConstants.HomeConfigBottomSheet.SETTINGS) {
                if (it != -1) {
                    // bottomSheet.dismiss()
                    if (this::mView.isInitialized) {
                        Navigation.findNavController(mView)
                            .navigate(R.id.action_homeFragment_to_settingsFragment)
                    }


                    sharedViewModel._homeConfigBottomSheetClickId.value = -1
                }
            } else if (it == AppConstants.HomeConfigBottomSheet.UPDATE_LOCATION) {
                if (it != -1) {
                    //call api to select country
                    mViewModel.hitUpdateUserLocationFromHome(sharedViewModel.userUpdatedCountryId)

                    sharedViewModel._homeConfigBottomSheetClickId.value = -1
                }
            }

        })

        sharedViewModel.homeItemBottomSheetClickId.observe(this, Observer {
            if (it == AppConstants.HomeItemBottomSheet.BLOCK_VENDOR) {
                if (it != -1) {
                    mViewModel.hitBlockAccountApi(31)
                    sharedViewModel.homeItemBottomSheetClickId.value = -1
                }
            }
        })
    }


    //Home Screen RecyclearViews Items Clicks
    override fun onItemClickListener(type: String, id :Int) {
        when (type) {
            AppConstants.RecyclerViewKeys.HOME_RV -> {
                sharedViewModel.vendorProfileId = id
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
                    //hit again to refresh data
                    mViewModel.hitGetAllCountriesForHome()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })


        mViewModel.blockAccountResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    mViewModel.hitGetHomeScreenInfoApi()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.getHomeScreenResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                   // loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                   // loadingDialog.dismiss()
                    getHomeScreenResponse = it.data!!

                    setData(getHomeScreenResponse)
                    homerRecyclerAdpater.notifyDataSetChanged()
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                    recycler_home.visibility = View.VISIBLE
                    rl_promotion.visibility = View.VISIBLE


                }
                Resource.Status.ERROR -> {
                    shimmerFrameLayout.visibility = View.GONE
                    recycler_home.visibility = View.VISIBLE
                    rl_promotion.visibility = View.VISIBLE
                   // loadingDialog.dismiss()
                   // DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

    }

    private fun setData(homeScreenResponse: GetHomeScreenResponse){
        try {
            Picasso.get().load(homeScreenResponse.promotion.banner).fit().centerCrop().into(iv_home_banner , object :
                Callback {
                override fun onSuccess() {
                    spinKit.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    if(iv_home_banner != null){
                        Picasso.get().load(R.drawable.placeholder_image).into(iv_home_banner)
                        spinKit.visibility = View.GONE
                    }

                }

            })
            tv_bannerTimer.text = homeScreenResponse.promotion.offer_end
            getHomeScreenData1 = homeScreenResponse.data
            //sharedViewModel.vendorProfileId = getHomeScreenData.id

            if(!homeScreenResponse.data.isNullOrEmpty()){
                vendorsDataList.clear()
                vendorsDataList.addAll(homeScreenResponse.data)
                homerRecyclerAdpater.notifyDataSetChanged()
            }

            



        } catch (e: Exception) {
        }

    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmer()
        super.onPause()
    }

    //vendor post item clicked
    override fun onChildItemClick(position: Int) {

        Navigation.findNavController(iv_icon).navigate(R.id.action_homeFragment_to_postDetailFragment)
    }

    private fun subscribeToObserveDarkActivation() {

        //observing data from data store and showing
        dataStoreProvider.darkModeFlow.asLiveData().observe(viewLifecycleOwner, Observer {
            //  switch_darkMode.isChecked = it
            if (it != null) {
                if (it == true) {
                    iv_icon.setImageDrawable(resources.getDrawable(R.drawable.app_icon_dark))
                }
            }
        })

    }
}