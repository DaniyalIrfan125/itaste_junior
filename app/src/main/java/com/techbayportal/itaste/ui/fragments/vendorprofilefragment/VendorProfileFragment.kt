package com.techbayportal.itaste.ui.fragments.vendorprofilefragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.opensooq.supernova.gligar.GligarPicker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetAllCitiesData
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.VendorPersonalProfileResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentVendorProfileBinding
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.ProfileSpinnerAdapter
import com.techbayportal.itaste.utils.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_vendor_profile.*
import kotlinx.android.synthetic.main.fragment_vendor_profile.et_country_code
import kotlinx.android.synthetic.main.layout_signupvendorfragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class VendorProfileFragment : BaseFragment<FragmentVendorProfileBinding, VendorProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_vendor_profile
    override val viewModel: Class<VendorProfileViewModel>
        get() = VendorProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var countrySpinnerAdapter: ProfileSpinnerAdapter<GetAllCountriesData>
    lateinit var citiesSpinnerAdapter: ProfileSpinnerAdapter<GetAllCitiesData>
    lateinit var vendorPersonalProfileResponseData: VendorPersonalProfileResponseData
    var profileImageFile: File? = null
    private lateinit var mView: View

    private val countriesList = arrayListOf<GetAllCountriesData>()
    private val citiesList = arrayListOf<GetAllCitiesData>()
    var countryid: String = "0"
    var selectedCountryId: String = "0"
    var cityid: String = "0"

    var compressedProfileImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.hitGetVendorPersonalProfile()
        subscribeToNetworkLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldTextWatcher()
        setCountriesData()
        mView = view
        if (this::vendorPersonalProfileResponseData.isInitialized) {
            setData(vendorPersonalProfileResponseData)
        }


        et_country_code.registerPhoneNumberTextView(et_vendorPhoneNumber)
        et_vendorPhoneNumber.hint = "5472227999"


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            if (requestCode == AppConstants.VENDOR_PROFILE_PIC_CODE) {
                val imagePath = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)!![0]
                profileImageFile = File(imagePath)
                if (profileImageFile != null) {
                    compressImageFile(profileImageFile)
                    Picasso.get()
                        .load(profileImageFile!!)
                        .fit()
                        .centerCrop()
                        .into(siv_vendorProfilePic)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun compressImageFile(imageFile: File?) {
        imageFile?.let {
            GlobalScope.launch {
                compressedProfileImageFile =
                    Compressor.compress(requireContext(), profileImageFile!!) {
                        quality(70)
                        format(Bitmap.CompressFormat.WEBP)
                        size(2_097_152) //2MB

                    }
            }
        }

    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getVendorPersonalProfileResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    vendorPersonalProfileResponseData = it.data!!.data
                    setData(vendorPersonalProfileResponseData)

                    //sharedViewModel.verifyOtpHoldPhoneNumber = et_country_code.fullNumberWithPlus

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

        mViewModel.getUpdateVendorPersonalProfileResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    //hit api again to get updated data

                    if (et_country_code.fullNumberWithPlus != vendorPersonalProfileResponseData.phone) {
                        sharedViewModel.isUpdatePhone = true
                        sharedViewModel.isForGotVerify = false
                        sharedViewModel.verifyOtpHoldNewPhoneNumber =
                            et_country_code.fullNumberWithPlus
                        Navigation.findNavController(mView)
                            .navigate(R.id.action_vendorProfileFragment_to_otpverificationFragment2)

                    } else {
                        mViewModel.hitGetVendorPersonalProfile()
                    }

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })


        mViewModel.getCountriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    countriesList.clear()
                    countriesList.addAll(it.data!!.data)
                    countrySpinnerAdapter.notifyDataSetChanged()

                    for (index in 0 until countriesList.size) {
                        if (countriesList[index].select) {
                           var countryidL = countriesList[index].id
                            sp_vendorCountry.setSelection(index)
                            mViewModel.getAllCities(countryidL.toInt())

                            return@Observer
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.getCitiesResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    citiesList.clear()
                    citiesList.addAll(it.data!!.data)
                    mViewDataBinding.spVendorCity.adapter = ProfileSpinnerAdapter(citiesList)

                    for (index in 0 until citiesList.size) {
                        if (citiesList[index].select) {
                            cityid = citiesList[index].id
                            sp_vendorCity.setSelection(index)

                            return@Observer
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


    }


    private fun validateFields() {
        if (et_vendorFirstName.text.isNotEmpty()) {
            if (et_vendorLastName.text.isNotEmpty()) {
                if (et_vendorBio.text.isNotEmpty()) {
                 //   if (et_country_code.isNotEmpty()) {
                    if (et_vendorPhoneNumber.text.isNotEmpty()) {
                        if (et_vendorEmail.text.isNotEmpty()) {
                            if (Patterns.EMAIL_ADDRESS.matcher(et_vendorEmail.text.toString())
                                    .matches()
                            ) {
                                if (sp_vendorCountry.selectedItemPosition != -1) {
                                    tv_error_vendorCountry.visibility = View.GONE
                                    if (sp_vendorCity.selectedItemPosition != -1 && cityid != "0") {
                                        tv_error_vendorCity.visibility = View.GONE
                                        if (profileImageFile != null) {

                                            if (compressedProfileImageFile != null) {
                                                mViewModel.hitUpdateVendorPersonalProfile(
                                                    et_vendorFirstName.text.toString(),
                                                    et_vendorLastName.text.toString(),
                                                    et_vendorBio.text.toString(),
                                                    et_country_code.fullNumberWithPlus,
                                                    et_vendorEmail.text.toString(),
                                                    profileImageFile!!,
                                                    selectedCountryId,
                                                    cityid
                                                )
                                            }else{
                                                Toast.makeText(
                                                    requireContext(),
                                                    "File compressing...Try again",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            mViewModel.hitUpdateVendorPersonalProfile(
                                                et_vendorFirstName.text.toString(),
                                                et_vendorLastName.text.toString(),
                                                et_vendorBio.text.toString(),
                                                et_country_code.fullNumberWithPlus,
                                                et_vendorEmail.text.toString(),
                                                null,
                                                selectedCountryId,
                                                cityid
                                            )
                                        }
                                    } else {
                                        tv_error_vendorCity.text = getString(R.string.selectcity)
                                        tv_error_vendorCity.visibility = View.VISIBLE
                                    }
                                } else {
                                    tv_error_vendorCountry.text = getString(R.string.selectcountry)
                                    tv_error_vendorCountry.visibility = View.VISIBLE
                                }
                            } else {
                                tv_error_vendorEmail.text = getString(R.string.writevalidemail)
                                tv_error_vendorEmail.visibility = View.VISIBLE
                            }
                        } else {
                            tv_error_vendorEmail.text = getString(R.string.PleasewriteEmail)
                            tv_error_vendorEmail.visibility = View.VISIBLE
                        }
                   /* } else {
                        //et_country_code.setDefaultCountryUsingNameCode("AE")
                        //tv_error_vendorPhoneNumber.text = getString(R.string.selectCode)
                       // tv_error_vendorPhoneNumber.visibility = View.VISIBLE
                    }*/

                    } else {
                        tv_error_vendorPhoneNumber.text = getString(R.string.writephone)
                        tv_error_vendorPhoneNumber.visibility = View.VISIBLE
                    }

                } else {
                    tv_error_vendorBio.text = getString(R.string.writebio)
                    tv_error_vendorBio.visibility = View.VISIBLE
                }
            } else {
                tv_error_vendorLastName.text = getString(R.string.writelastname)
                tv_error_vendorLastName.visibility = View.VISIBLE
            }

        } else {
            tv_error_vendorFirstName.text = getString(R.string.writefirstname)
            tv_error_vendorFirstName.visibility = View.VISIBLE
        }
    }

    private fun fieldTextWatcher() {

        et_vendorFirstName.doOnTextChanged { text, start, before, count ->
            tv_error_vendorFirstName.visibility = View.GONE
            //et_vendorFirstName.setTextColor(resources.getColor(R.color.colorErrorRed))
        }

        et_vendorLastName.doOnTextChanged { text, start, before, count ->
            tv_error_vendorLastName.visibility = View.GONE
        }


        et_vendorBio.doOnTextChanged { text, start, before, count ->
            tv_error_vendorBio.visibility = View.GONE
        }

        et_vendorPhoneNumber.doOnTextChanged { text, start, before, count ->
            tv_error_vendorPhoneNumber.visibility = View.GONE
        }

        et_vendorEmail.doOnTextChanged { text, start, before, count ->
            tv_error_vendorEmail.visibility = View.GONE
        }


    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(requireView()).popBackStack()
        })

        mViewModel.onSaveChangesButtonClicked.observe(this, Observer {
            validateFields()
        })

        mViewModel.onProfilePicChangeTextClicked.observe(this, Observer {
            GligarPicker().requestCode(AppConstants.VENDOR_PROFILE_PIC_CODE)
                .withFragment(this)
                .limit(1)
                .show()
        })

        sp_vendorCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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

        sp_vendorCountry.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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
                        selectedCountryId = countriesList[position].id
                        mViewModel.getAllCities(selectedCountryId.toInt())

                    }
                }
            }
    }

    private fun setData(vendorPersonalProfileResponseData: VendorPersonalProfileResponseData) {
        try {
            tv_vendorName.text =
                "${vendorPersonalProfileResponseData.first_name} ${vendorPersonalProfileResponseData.last_name}"
            et_vendorFirstName.setText(vendorPersonalProfileResponseData.first_name)
            et_vendorLastName.setText(vendorPersonalProfileResponseData.last_name)
            et_vendorBio.setText(vendorPersonalProfileResponseData.bio)
            et_country_code.fullNumber = vendorPersonalProfileResponseData.phone
            sharedViewModel.verifyOtpHoldPhoneNumber =
                vendorPersonalProfileResponseData.phone
            et_vendorEmail.setText(vendorPersonalProfileResponseData.email)
            Picasso.get()
                .load(vendorPersonalProfileResponseData.profile_pic).fit().centerCrop()
                .into(siv_vendorProfilePic, object :Callback{
                    override fun onSuccess() {
                        sk_vendorProfile.visibility = View.GONE
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image).into(siv_vendorProfilePic)
                        sk_vendorProfile.visibility = View.GONE
                    }

                })
        } catch (e: Exception) {
        }
    }

    private fun setCountriesData() {
        countrySpinnerAdapter = ProfileSpinnerAdapter(countriesList)
        mViewDataBinding.spVendorCountry.adapter = countrySpinnerAdapter

    }

    private fun setCitiesData() {
        citiesSpinnerAdapter = ProfileSpinnerAdapter(citiesList)
        mViewDataBinding.spVendorCity.adapter = citiesSpinnerAdapter

    }
}

