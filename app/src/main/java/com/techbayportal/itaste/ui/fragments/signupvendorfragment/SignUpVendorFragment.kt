package com.techbayportal.itaste.ui.fragments.signupvendorfragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.GetAllCountriesResponse
import com.techbayportal.itaste.data.models.getCitiesInputModel
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupvendorfragmentBinding
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.img_back

@AndroidEntryPoint
class SignUpVendorFragment :
    BaseFragment<LayoutSignupvendorfragmentBinding, SignupVendorViewModel>() {

    override val layoutId: Int
        get() = R.layout.layout_signupvendorfragment
    override val viewModel: Class<SignupVendorViewModel>
        get() = SignupVendorViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private val countriesList= arrayListOf<GetAllCountriesData> ()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_days.adapter = DaysRecyclerAdapter(
            listOf<String>(
              "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"),
            requireContext()
        )
        recycler_days.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

    }



    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.getAllCountries()

        spinner_country.onItemSelectedListener =object: AdapterView.OnItemSelectedListener{
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

        mViewModel.getCountriesResponse.observe(this, Observer {
          //  Snackbar.make(requireView(), "Country", Snackbar.LENGTH_SHORT).show()
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    mViewDataBinding.spinnerCountry.adapter= SpinnerAdapter(it.data!!.data)
                    // paste api call here
                    countriesList.addAll(it.data!!.data)
                    Toast.makeText(requireContext(), "Countries Loaded", Toast.LENGTH_SHORT).show()
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
                    mViewDataBinding.spinnerCity.adapter= SpinnerAdapter(it.data!!.data)
                    // paste api call here
                    Toast.makeText(requireContext(), "Cities Loaded", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }

            }

        })


        mViewModel.onSignUpVendorButtonClicked.observe(this, Observer {

           // fieldValidations()


            /* Navigation.findNavController(btn_signUp)
                 .navigate(R.id.action_signUpFragment_to_signupConfigurationsFragment)*/

        })

        /*mViewModel.onChooseCitySpinnerClicked.observe(this, Observer {
          //  Snackbar.make(requireView(), "City", Snackbar.LENGTH_SHORT).show()
        })*/
    }
}