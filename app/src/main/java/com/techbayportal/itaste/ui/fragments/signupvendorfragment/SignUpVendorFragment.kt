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

    var profileImageFile: File? = null

    var deliveriable : Boolean = false

    //var array3: ArrayList<String> = ArrayList()

    var array3: Array<String> = arrayOf("Mashroom", "Kitkat", "Oreo", "Lolipop")

    val listofweek = ArrayList<String>()




    private var selectedDayItems: MutableList<DaysOfWeek> = mutableListOf()
  // private var selectedDayItems: ArrayList<DaysOfWeek> = ArrayList()
    private var selectedDayItems2: MutableList<DaysOfWeek> = mutableListOf()
    private var actionMode: ActionMode? = null
    private lateinit var adapter: DaysRecyclerAdapter
    private lateinit var adapter2: DaysRecyclerAdapter
    private var tracker: SelectionTracker<DaysOfWeek>? = null
    private var tracker2: SelectionTracker<DaysOfWeek>? = null

    private val countriesList = arrayListOf<GetAllCountriesData>()
    private var listView: ListView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // array3.add("mon")

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

                        Toast.makeText(
                            context,
                            "checked: $selectedDayItems + ${selectedDayItems.size} :Days",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (selectedDayItems.isEmpty() && selectedDayItems2.isEmpty()) {
                            //  actionMode?.finish()
                        } else {
                            /*if (actionMode == null) actionMode =
                                activity.startSupportActionMode(this@MainActivity)
                            actionMode?.title =
                                "${selectedPostItems.size + selectedPostItems2.size}"*/
                        }
                    }
                }
            })


        /*recycler_days.adapter = DaysRecyclerAdapter(
            listOf<String>(
              "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"),
            requireContext()
        )
        recycler_days.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
*/

    }

    fun fieldValidations() {
        if (spinner_country != null && spinner_country.selectedItem != null) {
            if (spinner_city != null && spinner_city.selectedItem != null) {
                if (rg_service_type != null && rg_service_type.checkedRadioButtonId == -1) {
                    Toast.makeText(requireContext(), "All fields filled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        //  mViewModel.getAllCountries()

        spinner_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                countriesList[position].id
                mViewModel.getAllCities(countriesList[position].id.toInt())
            }

        }

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onServiceTypeRadioButtonClicked.observe(this, Observer {
            val selectedRadioButtonId: Int = rg_service_type.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                when {
                    rb_deliverable.isChecked -> {
                        deliveriable
                    }
                    rb_not_deliverable.isChecked -> {
                        !deliveriable
                    }
                    else -> {
                        Snackbar.make(requireView(), "No service selected", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(requireView(), "Please select your service type", Snackbar.LENGTH_SHORT).show()
            }
        })


        mViewModel.getCountriesResponse.observe(this, Observer {
            //  Snackbar.make(requireView(), "Country", Snackbar.LENGTH_SHORT).show()
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    mViewDataBinding.spinnerCountry.adapter = SpinnerAdapter(it.data!!.data)
                    // paste api call here
                    countriesList.addAll(it.data!!.data)
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
                    mViewDataBinding.spinnerCity.adapter = SpinnerAdapter(it.data!!.data)
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
                    sharedViewModel.verifyOtpHoldPhoneNumber = et_country_code.selectedCountryCodeWithPlus+ ed_phoneNumber.text.toString()
                    sharedViewModel.isForGotVerify = false
                    //  Navigation.findNavController(mView).navigate(R.id.action_signupFragment_to_verifyOtpFragment)

                    //on signup success navigate to OTP screen
                    Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_otpverificationFragment)

                    // Toast.makeText(requireContext(), "Sign Up Success", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })


        mViewModel.onSignUpVendorButtonClicked.observe(this, Observer {

            // fieldValidations()

           // sharedViewModel.userModel = dataSetUserModel()
           // mViewModel.signUpVendorAPICall(dataSetUserModel())

            listofweek.add("mon")
            listofweek.add("tue")

            if (sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR) {
                sharedViewModel.userModel?.let {
                    it.country_id = "" + 1
                    it.city_id = "" + 1
                    it.days_of_week = listofweek
                    it.is_deliverable = deliveriable
                    it.password_confirmation = "1"
                    mViewModel.signUpVendorAPICall(it)

                }
            }


            /* Navigation.findNavController(btn_signUp)
                 .navigate(R.id.action_signUpFragment_to_signupConfigurationsFragment)*/

        })

        /*mViewModel.onChooseCitySpinnerClicked.observe(this, Observer {
          //  Snackbar.make(requireView(), "City", Snackbar.LENGTH_SHORT).show()
        })*/
    }

    private fun dataSetUserModel(): UserModel {
        val userModel = UserModel()
        userModel.first = ed_firstName.text.toString()
        userModel.last = ed_lastName.text.toString()
        userModel.username = ed_userName.text.toString()
        userModel.profileImage = profileImageFile!!
        userModel.phone =
            et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()

        userModel.email = ed_email.text.toString()
        userModel.password = ed_setPassword.text.toString()
        userModel.country_id = spinner_country.selectedItem.toString()
        userModel.city_id = spinner_city.selectedItem.toString()
       // userModel.days_of_week = arrayOf("sunday")

        //arrayOf("sunday").toCollection(ArrayList())
        userModel.is_deliverable = deliveriable
        userModel.password_confirmation = ed_confirmPassword.text.toString()
        return userModel
    }
}