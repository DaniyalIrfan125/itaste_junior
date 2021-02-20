package com.techbayportal.itaste.ui.fragments.signupfragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.opensooq.supernova.gligar.GligarPicker
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.models.UserModel
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupfragmentBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size

import kotlinx.android.synthetic.main.layout_signupfragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.io.File


@AndroidEntryPoint
class SignUpFragment : BaseFragment<LayoutSignupfragmentBinding, SignUpFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupfragment
    override val viewModel: Class<SignUpFragmentViewModel>
        get() = SignUpFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var profileImageFile: File? = null
    var compressedProfileImageFile: File? = null
    private lateinit var mView: View
    lateinit var dataStoreProvider: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStoreProvider = DataStoreProvider(requireContext())
        mView = view
        fieldTextWatcher()
        checkUserType()

        et_country_code.registerPhoneNumberTextView(ed_phoneNumber);
        ed_phoneNumber.hint = "547222799";


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == AppConstants.PROFILE_PIC_CODE) {
                val imagePath = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)!![0]
                profileImageFile = File(imagePath)
                if (profileImageFile != null) {
                    //calling method to compress image
                    compressImageFile(profileImageFile)
                    Picasso.get()
                        .load(profileImageFile!!)
                        .fit()
                        .centerCrop()
                        .into(siv_profile_pic)
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

    override fun onResume() {
        super.onResume()

        if (profileImageFile != null) {
            Picasso.get()
                .load(profileImageFile!!)
                .fit()
                .centerCrop()
                .into(siv_profile_pic)
        }
    }

    private fun fieldTextWatcher() {

        ed_firstName.doOnTextChanged { text, start, before, count ->
            tv_errorName.visibility = View.GONE
            ed_firstName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_lastName.doOnTextChanged { text, start, before, count ->
            tv_errorName.visibility = View.GONE
            ed_lastName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }


        ed_userName.doOnTextChanged { text, start, before, count ->
            tv_errorUsername.visibility = View.GONE
            ed_userName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_phoneNumber.doOnTextChanged { text, start, before, count ->
            tv_errorPhoneNumber.visibility = View.GONE
            ed_phoneNumber.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_setPassword.doOnTextChanged { text, start, before, count ->
            tv_errorSetPassword.visibility = View.GONE
            ed_setPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_confirmPassword.doOnTextChanged { text, start, before, count ->
            tv_errorConfirmPassword.visibility = View.GONE
            ed_confirmPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }


        ed_email.doOnTextChanged { text, start, before, count ->
            tv_errorEmail.visibility = View.GONE
            ed_email.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

    }


    fun fieldValidations() {
        if (!TextUtils.isEmpty(ed_firstName.text)) {
            if (!TextUtils.isEmpty(ed_lastName.text)) {
                if (!TextUtils.isEmpty(ed_userName.text)) {

                    if (!TextUtils.isEmpty(ed_email.text)) {

                        if (Patterns.EMAIL_ADDRESS.matcher(ed_email.text.toString()).matches()) {

                            if (!TextUtils.isEmpty(ed_phoneNumber.text)) {

                                    if (!TextUtils.isEmpty(ed_setPassword.text) && ed_setPassword.text.toString().length > 5) {

                                        if (!TextUtils.isEmpty(ed_confirmPassword.text) && ed_confirmPassword.text.toString().length > 5) {

                                            if (ed_setPassword.text.toString() == ed_confirmPassword.text.toString()) {
                                                //  when all Edit texts checks are full filled


                                                //check for profile pic
                                                if (profileImageFile == null) {
                                                    Snackbar.make(
                                                        mView,
                                                        getString(R.string.Profile_pic_is_null),
                                                        Snackbar.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    if (sharedViewModel.userType == AppConstants.UserTypeKeys.USER) {
                                                        try {
                                                            if (compressedProfileImageFile != null) {
                                                                sharedViewModel.userModel.let {
                                                                    it.first =
                                                                        ed_firstName.text.toString()
                                                                    it.last =
                                                                        ed_lastName.text.toString()
                                                                    it.username =
                                                                        ed_userName.text.toString()
                                                                    it.phone =
                                                                        et_country_code.fullNumberWithPlus
                                                                    //et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()
                                                                    it.profileImage =
                                                                        profileImageFile!!
                                                                    it.email =
                                                                        ed_email.text.toString()
                                                                    it.password =
                                                                        ed_confirmPassword.text.toString()
                                                                    it.role =
                                                                        AppConstants.UserTypeKeys.USER
                                                                    it.password_confirmation =
                                                                        ed_confirmPassword.text.toString()
                                                                    mViewModel.signUpAPICall(it)
                                                                    sharedViewModel.userModel =
                                                                        UserModel()
                                                                }
                                                            } else {
                                                                Toast.makeText(
                                                                    requireContext(),
                                                                    "File compressing...Try again",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }

                                                        } catch (e: Exception) {
                                                        }

                                                    } else if (sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR) {
                                                        sharedViewModel.verifyOtpHoldPhoneNumber = et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()
                                                        sharedViewModel.userModel = dataSetUserSignUpModel()
                                                        Navigation.findNavController(btn_signUp).navigate(R.id.action_signUpFragment_to_signUpVendorFragment)
                                                    }
                                                }

                                            } else {
                                                tv_errorConfirmPassword.visibility = View.VISIBLE
                                                ed_confirmPassword.background =
                                                    ContextCompat.getDrawable(
                                                        requireContext(),
                                                        R.drawable.ed_errorboundary
                                                    )
                                                tv_errorConfirmPassword.text =
                                                    getString(R.string.Passwords_does_not_match)
                                            }
                                        } else {

                                            if (ed_confirmPassword.text.toString().length in 1..5) {
                                                tv_errorConfirmPassword.text =
                                                    getString(R.string.password_at_least_six_character)
                                            } else {
                                                tv_errorConfirmPassword.text =
                                                    getString(R.string.Pleasewriteconfirm_password)
                                            }
                                            tv_errorConfirmPassword.visibility = View.VISIBLE
                                            ed_confirmPassword.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.ed_errorboundary
                                                )

                                        }


                                    } else {
                                        if (ed_setPassword.text.toString().length in 1..5) {
                                            tv_errorSetPassword.text =
                                                getString(R.string.password_at_least_six_character)
                                        } else {
                                            tv_errorSetPassword.text =
                                                getString(R.string.Pleasewritepassword)
                                        }
                                        tv_errorSetPassword.visibility = View.VISIBLE
                                        ed_setPassword.background = ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.ed_errorboundary
                                        )

                                    }


                            } else {
                                tv_errorPhoneNumber.text =
                                    getString(R.string.Pleasewritephonenumber)

                                tv_errorPhoneNumber.visibility = View.VISIBLE
                                ed_phoneNumber.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ed_errorboundary
                                    )


                            }
                        } else {

                            tv_errorEmail.visibility = View.VISIBLE
                            ed_email.background =
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ed_errorboundary
                                )
                            tv_errorEmail.text = "Please write a Valid Email!"

                        }

                    } else {

                        tv_errorEmail.visibility = View.VISIBLE
                        ed_email.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                        tv_errorEmail.text = getString(R.string.PleasewriteEmail)


                    }


                } else {

                    tv_errorUsername.visibility = View.VISIBLE
                    ed_userName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                    tv_errorUsername.text = getString(R.string.PleasewriteUsername)

                }


            } else {
                tv_errorName.visibility = View.VISIBLE
                ed_lastName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                tv_errorName.text = getString(R.string.PleasewriteLastName)
            }

        } else {
            tv_errorName.visibility = View.VISIBLE
            ed_firstName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_errorName.text = getString(R.string.PleasewriteFirstName)
        }
    }

    private fun dataSetUserSignUpModel(): UserModel {
        val addDataSetUserSignUpModel = UserModel()
        addDataSetUserSignUpModel.first = ed_firstName.text.toString()
        addDataSetUserSignUpModel.last = ed_lastName.text.toString()
        addDataSetUserSignUpModel.username = ed_userName.text.toString()
        addDataSetUserSignUpModel.phone =
            et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()
        addDataSetUserSignUpModel.profileImage = profileImageFile!!
        addDataSetUserSignUpModel.email = ed_email.text.toString()
        addDataSetUserSignUpModel.password = ed_confirmPassword.text.toString()
        addDataSetUserSignUpModel.role = AppConstants.UserTypeKeys.USER
        addDataSetUserSignUpModel.password_confirmation = ed_confirmPassword.text.toString()
        //mViewModel.signUpAPICall(it)
        return addDataSetUserSignUpModel
    }

    override fun subscribeToNetworkLiveData() {
        mViewModel.signUpResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    sharedViewModel.verifyOtpHoldPhoneNumber =
                        et_country_code.selectedCountryCodeWithPlus + ed_phoneNumber.text.toString()
                    sharedViewModel.isForGotVerify = false
                    sharedViewModel.isUpdatePhone = false

                    //on signup success navigate to OTP screen
                    Navigation.findNavController(mView)
                        .navigate(R.id.action_signUpFragment_to_otpverificationFragment)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onProfilePicTextClicked.observe(this, Observer {
            GligarPicker().requestCode(AppConstants.PROFILE_PIC_CODE)
                .withFragment(this)
                .limit(1)
                .show()
        })

        mViewModel.onSignUpButtonClicked.observe(this, Observer {
            fieldValidations()
        })
    }

    //observing User type vendor or user
    private fun checkUserType() {
        if (sharedViewModel.userType == AppConstants.UserTypeKeys.USER) {


        } else if (sharedViewModel.userType == AppConstants.UserTypeKeys.VENDOR) {
            btn_signUp.text = getString(R.string.next)
        }

    }


}