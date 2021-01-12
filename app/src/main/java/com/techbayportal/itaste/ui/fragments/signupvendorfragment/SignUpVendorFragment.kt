package com.techbayportal.itaste.ui.fragments.signupvendorfragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.selection.SelectionPredicates
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
import com.techbayportal.itaste.data.models.DaysOfWeek
import com.techbayportal.itaste.data.models.GetAllCitiesData
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.UserModel
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupvendorfragmentBinding
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider.MyItemDetailsLookup
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider.MyItemKeyProvider
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.img_back
import java.io.File


@AndroidEntryPoint
class SignUpVendorFragment :
    BaseFragment<LayoutSignupvendorfragmentBinding, SignupVendorViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_signupvendorfragment
    override val viewModel: Class<SignupVendorViewModel>
        get() = SignupVendorViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var countryid: String = "0"
    var cityid: String = "0"


    var profileImageFile: File? = null

    // var deliveriable : Boolean = false
    var isDeliveriable: Int? = null
    var array3: Array<String> = arrayOf("Mashroom", "Kitkat", "Oreo", "Lolipop")

    //sample array for test
    val listofweek = ArrayList<String>()

    //original array for use in api
    val listofweekDays = ArrayList<String>()


    private var selectedDayItems: MutableList<DaysOfWeek> = mutableListOf()
    private var selectedDayItems2: MutableList<DaysOfWeek> = mutableListOf()
    private lateinit var adapter: DaysRecyclerAdapter
    private var tracker: SelectionTracker<DaysOfWeek>? = null
    private val countriesList = arrayListOf<GetAllCountriesData>()
    private val citiesList = arrayListOf<GetAllCitiesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //for sample array
        listofweek.add("mon")
        listofweek.add("tue")

        //add static first item in Country list and cities list
        countriesList.add(0, GetAllCountriesData("0", "Select Country", "", ""))
        citiesList.add(0, GetAllCitiesData("0", "Select City", ""))

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


        tracker = SelectionTracker.Builder<DaysOfWeek>(
            "mySelection",
            daysRecyclerView,
            MyItemKeyProvider(adapter),
            MyItemDetailsLookup(daysRecyclerView),
            StorageStrategy.createParcelableStorage(DaysOfWeek::class.java)
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Short>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    tracker?.let {
                        selectedDayItems = it.selection.toMutableList()
                        selectedDayItems.remove(DaysOfWeek(""))
                        // Toast.makeText(context, "checked: $selectedDayItems + ${selectedDayItems.size} :Days", Toast.LENGTH_SHORT).show()
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


        /*recycler_days.adapter = DaysRecyclerAdapter(
            listOf<String>(
              "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"),
            requireContext()
        )
        recycler_days.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
*/

    }

    fun signUpVendorFieldValidations() {
        if (spinner_country.selectedItemPosition > 0) {
            if (spinner_city.selectedItemPosition > 0) {
                if (selectedDayItems.isNotEmpty()) {
                    if (isDeliveriable != null) {
                        // Toast.makeText(requireContext(), "LetsGo", Toast.LENGTH_SHORT).show()

                        selectedDayItems.forEach {
                            listofweekDays.add(it.dayName.toLowerCase())
                        }

                        if (sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR) {
                            sharedViewModel.userModel?.let {
                                it.country_id = countryid
                                it.city_id = cityid
                                it.days_of_week = listofweekDays
                                it.is_deliverable = "" + isDeliveriable
                                it.description = et_vendorDescription.text.toString()
                                mViewModel.signUpVendorAPICall(it)

                                // sharedViewModel.userModel = dataSetVendorModel()
                            }
                        }
                    } else {
                        //Toast.makeText(requireContext(), "Please select your delivery types!", Toast.LENGTH_SHORT).show()
                        Snackbar.make(
                            requireView(),
                            "Please select your delivery types!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    //Toast.makeText(requireContext(), "Please select your days of work!", Toast.LENGTH_SHORT).show()
                    Snackbar.make(
                        requireView(),
                        "Please select your days of work!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                //Toast.makeText(requireContext(), "Please select your city!", Toast.LENGTH_SHORT).show()
                Snackbar.make(requireView(), "Please select your city!", Snackbar.LENGTH_SHORT)
                    .show()
            }
        } else {
            //Toast.makeText(requireContext(), "Please select your Country!", Toast.LENGTH_SHORT).show()
            Snackbar.make(requireView(), "Please select your Country!", Snackbar.LENGTH_SHORT)
                .show()
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
                    mViewDataBinding.spinnerCountry.adapter = SpinnerAdapter(countriesList)
                    // paste api call here
                    // Toast.makeText(requireContext(), "Countries Loaded", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
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
                    citiesList.addAll(it.data!!.data)
                    mViewDataBinding.spinnerCity.adapter = SpinnerAdapter(citiesList)

                    // paste api call here
                    // Toast.makeText(requireContext(), "Cities Loaded", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
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

                    //on sign up success navigate to OTP screen
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_signUpVendorFragment_to_otpverificationFragment2)
                    // Toast.makeText(requireContext(), "Sign Up Success", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })

    }

    private fun dataSetVendorModel(): UserModel {
        val userModel = UserModel()
        userModel.country_id = spinner_country.selectedItem.toString()
        userModel.city_id = spinner_city.selectedItem.toString()
        userModel.days_of_week = listofweekDays
        userModel.is_deliverable = "" + isDeliveriable
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
                    mViewModel.getAllCities(countriesList[position].id.toInt())
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
            // sharedViewModel.userModel = dataSetUserModel()
            // mViewModel.signUpVendorAPICall(dataSetUserModel())
            signUpVendorFieldValidations()
        })

    }
}