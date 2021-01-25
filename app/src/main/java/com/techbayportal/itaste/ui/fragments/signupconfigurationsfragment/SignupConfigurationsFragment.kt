package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupconfigurationsfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment.adapter.UserLocationAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupconfigurationsfragment.*
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*
import java.io.File

@AndroidEntryPoint
class SignupConfigurationsFragment :
    BaseFragment<LayoutSignupconfigurationsfragmentBinding, SignUpConfigurationsViewModel>() {
    private var isDarkMode: Boolean? = null
    private var languagePreference: String = ""
    override val layoutId: Int
        get() = R.layout.layout_signupconfigurationsfragment
    override val viewModel: Class<SignUpConfigurationsViewModel>
        get() = SignUpConfigurationsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    val countriesList = ArrayList<GetAllCountriesData>()
    lateinit var mView: View
    lateinit var userLocationAdapter: UserLocationAdapter
    var countryId: Int = 0

    var compressedProfileImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getAllCountries()
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()

        rg_language.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_english) {
                languagePreference = "en"
            }
            if (checkedId == R.id.rb_arabic) {
                languagePreference = "ar"
            }
        }
    }

    private fun initializing() {
        userLocationAdapter =
            UserLocationAdapter(requireContext(),countriesList, object : UserLocationAdapter.ClickItemListener {
                override fun onClicked(getAllCountriesData: GetAllCountriesData) {

                    //it will get the id of country on which we click . we will sed this id update Location APi
                    countryId = getAllCountriesData.id.toInt()
                    sharedViewModel.userUpdatedCountryId = countryId
                    //Update ui on click
                    Toast.makeText(requireContext(), "Location selected $countryId ${getAllCountriesData.name}", Toast.LENGTH_SHORT).show()
                }
            })

        rv_userLocation.adapter = userLocationAdapter
        // rv_userLocation.layoutManager = LinearLayoutManager(requireContext())
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
                    userLocationAdapter.notifyDataSetChanged()

                    // Toast.makeText(requireContext(), "Countries Loaded", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.updateUserLocationResponse.observe(this, Observer {
            //  Snackbar.make(requireView(), "Country", Snackbar.LENGTH_SHORT).show()
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()

                    //move to next screen
                    navigateToMainScreen()

                    Toast.makeText(
                        requireContext(),
                        "Location Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

    }

    fun signUpConfigFieldValidations() {
        if(languagePreference.isNotEmpty()) {
            if(isDarkMode != null){
                if(countryId !=0 ){

                        mViewModel.updateUserLocation(countryId)
                  //  }else{
                  //      Snackbar.make(requireView(), "No Token Received", Snackbar.LENGTH_SHORT).show()
                  //  }

                }else{
                    Snackbar.make(requireView(), "Please select a country", Snackbar.LENGTH_SHORT).show()
                }
            }else{
                Snackbar.make(requireView(), "Please select a mode", Snackbar.LENGTH_SHORT).show()
            }

        }else{
            Snackbar.make(requireView(), "Please select your preferred language", Snackbar.LENGTH_SHORT).show()
        }

    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onNextButtonClicked.observe(this, Observer {
            signUpConfigFieldValidations()
        })

        mViewModel.onDarkModeButtonClicked.observe(this, Observer {

            isDarkMode = true
            rl_dark_mode.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_light_mode.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_moon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            iv_sun.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray_image_color
                )
            )
        })

        mViewModel.onLightModeButtonClicked.observe(this, Observer {
            isDarkMode = false
            rl_light_mode.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.item_circleorangelayout)
            rl_dark_mode.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.item_circle_grey)

            iv_sun.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            iv_moon.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.gray_image_color
                )
            )
        })
    }

    //navigate to home screen
    private fun navigateToMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}