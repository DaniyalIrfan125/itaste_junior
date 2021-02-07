package com.techbayportal.itaste.ui.fragments.signupvendorfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupvendorfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider.MyItemDetailsLookup
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider.MyItemKeyProvider
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import com.techbayportal.itaste.utils.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.img_back
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpVendorFragment :
    BaseFragment<LayoutSignupvendorfragmentBinding, SignupVendorViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_signupvendorfragment
    override val viewModel: Class<SignupVendorViewModel>
        get() = SignupVendorViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var countryid: Int = 0
    var cityid: Int = 0

    var isDeliveriable: Int? = null

    //sample array for test
    val listofweek = ArrayList<String>()

    //original array for use in api
    val listofweekDays = ArrayList<String>()

    lateinit var dataStoreProvider: DataStoreProvider


    private var selectedDayItems: MutableList<DaysOfWeek> = mutableListOf()
    private lateinit var adapter: DaysRecyclerAdapter
    private var tracker: SelectionTracker<DaysOfWeek>? = null
    private val countriesList = ArrayList<GetAllCountriesData>()
    private val citiesList = ArrayList<GetAllCitiesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        //for sample array
        listofweek.add("mon")
        listofweek.add("tue")

        mViewModel.getAllCountries()

        //add static first item in Country list and cities list
        countriesList.add(0, GetAllCountriesData(0, "Select Country", "", true))
        citiesList.add(0, GetAllCitiesData(0, "Select City", true))

        val daysRecyclerView: RecyclerView = view.findViewById(R.id.recycler_days)
        val daysOfWeek: MutableList<DaysOfWeek> = mutableListOf()

        daysOfWeek.add(DaysOfWeek("Sunday"))
        daysOfWeek.add(DaysOfWeek("Monday"))
        daysOfWeek.add(DaysOfWeek("Tuesday"))
        daysOfWeek.add(DaysOfWeek("Wednesday"))
        daysOfWeek.add(DaysOfWeek("Thursday"))
        daysOfWeek.add(DaysOfWeek("Friday"))
        daysOfWeek.add(DaysOfWeek("Saturday"))

        adapter = DaysRecyclerAdapter(daysOfWeek, requireContext())
        recycler_days.adapter = adapter
        recycler_days.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        adapter.notifyDataSetChanged()


        tracker = SelectionTracker.Builder<DaysOfWeek>(
            "mySelection",
            daysRecyclerView,
            MyItemKeyProvider(adapter),
            MyItemDetailsLookup(daysRecyclerView),
            StorageStrategy.createParcelableStorage(DaysOfWeek::class.java)
        ).withSelectionPredicate(object : SelectionTracker.SelectionPredicate<DaysOfWeek>() {
            override fun canSelectMultiple(): Boolean = true
            override fun canSetStateForKey(key: DaysOfWeek, nextState: Boolean): Boolean =
                key != MyItemDetailsLookup.EMPTY_ITEM.selectionKey

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean =
                position != MyItemDetailsLookup.EMPTY_ITEM.position
        }).build()


        adapter.tracker = tracker



        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {

                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    tracker?.let {
                        if (it.hasSelection()) {
                            selectedDayItems = it.selection.toMutableList()
                            selectedDayItems.remove(DaysOfWeek(""))
                        }
                    }
                }
            })

        rg_service_type.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_deliverable) {
                isDeliveriable = 1
            }
            if (checkedId == R.id.rb_not_deliverable) {
                isDeliveriable = 0
            }
        }
    }

    private fun signUpVendorFieldValidations() {
        if (spinner_country.selectedItemPosition > 0) {
            if (spinner_city.selectedItemPosition > 0) {
              //  if (selectedDayItems.isNotEmpty()) {
                  //  if (isDeliveriable != null) {

                        selectedDayItems.forEach {
                            listofweekDays.add(it.dayName.toLowerCase())
                        }

                        // if (sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR) {

                        dataStoreProviderBase.switchToPremiumFlow.asLiveData()
                            .observe(this, Observer { switchToPremium ->
                                if (switchToPremium) {

                                    try {
                                        sharedViewModel.userModel.let {
                                            it.country_id = countryid
                                            it.city_id = cityid
                                            //it.days_of_week = listofweekDays
                                            // it.is_deliverable = isDeliveriable
                                            it.description = et_vendorDescription.text.toString()
                                            mViewModel.hitSwitchToPremiumApi(
                                                countryid,
                                                cityid,
                                              //  listofweekDays,
                                              //  isDeliveriable!!,
                                                et_vendorDescription.text.toString()
                                            )


                                        }
                                    } catch (e: Exception) {

                                    }


                                } else {
                                    try {
                                        sharedViewModel.userModel.let {
                                            it.country_id = countryid
                                            it.city_id = cityid
                                            //   it.days_of_week = listofweekDays
                                            //   it.is_deliverable = isDeliveriable
                                            it.description = et_vendorDescription.text.toString()
                                            mViewModel.signUpVendorAPICall(it)

                                            // sharedViewModel.userModel = dataSetVendorModel()
                                        }
                                    } catch (e: Exception) {

                                    }

                                }
                            })

                        //  }

                   /* } else {
                        Snackbar.make(
                            requireView(),
                            "Please select your delivery types!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Snackbar.make(
                        requireView(),
                        "Please select your days of work!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }*/
            } else {
                Snackbar.make(requireView(), "Please select your city!", Snackbar.LENGTH_SHORT)
                    .show()
            }
        } else {
            Snackbar.make(requireView(), "Please select your Country!", Snackbar.LENGTH_SHORT)
                .show()
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
                    countriesList.clear()
                    countriesList.add(0, GetAllCountriesData(0, "Select Country", "", true))
                    countriesList.addAll(it.data!!.data)
                    mViewDataBinding.spinnerCountry.adapter = SpinnerAdapter(countriesList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel._getCitiesResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    citiesList.clear()
                    citiesList.add(0, GetAllCitiesData(0, "Select City", true))
                    citiesList.addAll(it.data!!.data)
                    mViewDataBinding.spinnerCity.adapter = SpinnerAdapter(citiesList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }

        })

        mViewModel.signUpVendorResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    sharedViewModel.isForGotVerify = false
                    sharedViewModel.isUpdatePhone = false

                    //on sign up success navigate to OTP screen
                    Navigation.findNavController(requireView()).navigate(R.id.action_signUpVendorFragment_to_otpverificationFragment2)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.getSwitchToPremiumResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    //set Switch to Premium to false
                    /*GlobalScope.launch {
                        dataStoreProvider.switchToPremium(false)
                    }*/

                    val accessToken = LoginSession.getInstance().getLoginResponse()!!.data.access_token
                    //  val countryId = LoginSession.getInstance().getLoginResponse()!!.data.country_id
                    // val isPaymentUpdated = LoginSession.getInstance().getLoginResponse()!!.data.is_payment_update
                    val vendorUserName = LoginSession.getInstance().getLoginResponse()!!.data.username

                    if(it.data != null){
                        val userData = Data( it.data.data.id, it.data.data.first, it.data.data.last, vendorUserName, it.data.data.phone, it.data.data.email,
                            it.data.data.profile_pic,it.data.data.role, accessToken,it.data.data.country_id, it.data.data.is_payment_update)
                        val loginResponse = LoginResponse("update vendor profile", userData, "")
                        //LoginSession.getInstance().setLoginResponse(loginResponse)
                        mViewModel.saveUserObj(loginResponse)
                    }

                    navigateToMainScreen()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

    }

    //navigate to home screen
    private fun navigateToMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun dataSetVendorModel(): UserModel {
        val userModel = UserModel()
      ////  userModel.country_id = spinner_country.selectedItem.
      ////  userModel.city_id = spinner_city.selectedItem
       // userModel.days_of_week = listofweekDays
       // userModel.is_deliverable = isDeliveriable
        userModel.password_confirmation = ed_confirmPassword.text.toString()
        userModel.description = et_vendorDescription.text.toString()
        return userModel
    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()


        spinner_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                citiesList[position].id
                cityid = citiesList[position].id
            }
        }

        spinner_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    countriesList[position].id
                    mViewModel.getAllCities(countriesList[position].id)
                    countryid = countriesList[position].id
                }
            }
        }

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onServiceTypeRadioButtonClicked.observe(this, Observer {
        })

        mViewModel.onSignUpVendorButtonClicked.observe(this, Observer {
            signUpVendorFieldValidations()
        })

    }
}