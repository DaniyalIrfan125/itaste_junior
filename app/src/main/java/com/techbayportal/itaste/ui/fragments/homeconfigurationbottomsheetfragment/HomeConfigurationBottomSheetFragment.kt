package com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techbayportal.itaste.R
import com.techbayportal.itaste.SharedViewModel
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment.adapter.LocationsAdapter
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home_configuration_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_homefragment.*
import timber.log.Timber

class HomeConfigurationBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var dataStoreProvider: DataStoreProvider
    lateinit var sharedViewModel :SharedViewModel
    lateinit var locationAdapter: LocationsAdapter

    val locationsList = ArrayList<GetAllCountriesData>()
    lateinit var mView: View

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

        dataStoreProvider = DataStoreProvider(requireContext())
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

        subscribeToObserveLiveData()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_home_configuration_bottom_sheet, container, false)

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        subscribeToObserveDataStore()

        ll_settings.setOnClickListener(View.OnClickListener {
            dismiss()
            sharedViewModel._homeConfigBottomSheetClickId.postValue(AppConstants.HomeConfigBottomSheet.SETTINGS)

        })
        tv_turnOffNotifications.setOnClickListener(View.OnClickListener {
           // sharedViewModel._homeConfigBottomSheetClickId.value = AppConstants.HomeConfigBottomSheet.TURN_OFF_NOTIFICATION
          //  Toast.makeText(requireContext(), "notify 1", Toast.LENGTH_SHORT).show()

        })
        ll_contactUs.setOnClickListener(View.OnClickListener {
            dismiss()
            sharedViewModel._homeConfigBottomSheetClickId.value = AppConstants.HomeConfigBottomSheet.CONTACT_US

        })

        ll_logout.setOnClickListener(View.OnClickListener {
            sharedViewModel._homeConfigBottomSheetClickId.value = AppConstants.HomeConfigBottomSheet.LOGOUT


        })


        ll_change_language.setOnClickListener(View.OnClickListener {


            if (tv_language_name.text == getString(R.string.arabic)){
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("ar")
                startActivity(Intent(context, MainActivity::class.java))


            } else{
                activity?.finish()
                (activity as MainActivity?)!!.setLocaleLanguage("en")
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    )
                )
            }
        })
        initilizing()
    }

    private fun initilizing(){

        locationAdapter = LocationsAdapter(locationsList,object :LocationsAdapter.ClickItemListener{
            override fun onClicked(getAllCountriesData: GetAllCountriesData) {
                //Call Update Loc API
                sharedViewModel.userUpdatedCountryId = getAllCountriesData.id
                sharedViewModel._homeConfigBottomSheetClickId.postValue(AppConstants.HomeConfigBottomSheet.UPDATE_LOCATION)

            }

        })

        rv_userlocations.adapter = locationAdapter

        if (loginSession != null) {
            if (loginSession.data.role.equals(AppConstants.UserTypeKeys.VENDOR, true)) {
                // relative_addButton.background = null
                rv_userlocations.isVisible = false
                view_last.visibility = View.GONE


            }
        }

    }

    private fun subscribeToObserveLiveData(){
        sharedViewModel.countriesList.observe(this, Observer {
            it?.let{
                if(it.data.isNotEmpty()){
                    Timber.d("List empty")
                    locationsList.clear()
                    locationsList.addAll(it.data)
                    locationAdapter.notifyDataSetChanged()
                }
            }
        })
    }


        private fun subscribeToObserveDataStore() {
            //observing data from data store and showing
            dataStoreProvider.languageFlow.asLiveData().observe(this, Observer {

                if (it != null) {
                    if (it == "ar") {
                        tv_language_name.text = getString(R.string.english)


                    } else {
                        tv_language_name.text = getString(R.string.arabic)
                    }
                } else {
                    tv_language_name.text = getString(R.string.english)
                }
            })

        }





}