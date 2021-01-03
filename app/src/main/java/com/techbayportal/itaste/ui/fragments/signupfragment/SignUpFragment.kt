package com.techbayportal.itaste.ui.fragments.signupfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
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
import com.techbayportal.itaste.constants.AppConstants.PROFILE_PIC_CODE
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSignupfragmentBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_signupfragment.*
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
    private lateinit var mView: View


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        fieldTextWatcher()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.PROFILE_PIC_CODE) {
            val imagePath = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)!![0]
            profileImageFile = File(imagePath)

            if(profileImageFile != null)
                Picasso.get()
                .load(profileImageFile!!)
                .fit()
                .centerCrop()
                .into(siv_profile_pic);
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

                        if (!TextUtils.isEmpty(ed_phoneNumber.text)) {

                            if (!TextUtils.isEmpty(ed_setPassword.text)) {

                                if (!TextUtils.isEmpty(ed_confirmPassword.text)) {

                                    if (ed_setPassword.text.toString() == ed_confirmPassword.text.toString()) {

                                        /*mViewModel.signUpAPICall(
                                            ed_firstName.text.toString(),
                                            ed_lastName.text.toString(),
                                            ed_userName.text.toString(),
                                            ed_phoneNumber.text.toString(),
                                            "",
                                            ed_email.text.toString(),
                                            ed_password.text.toString(),
                                            "user"
                                        )*/


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

                                    tv_errorConfirmPassword.visibility = View.VISIBLE
                                    ed_confirmPassword.background =
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.ed_errorboundary
                                        )
                                    tv_errorConfirmPassword.text =
                                        getString(R.string.Pleasewriteconfirm_password)
                                }


                            } else {

                                tv_errorSetPassword.visibility = View.VISIBLE
                                ed_setPassword.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ed_errorboundary
                                    )
                                tv_errorSetPassword.text = getString(R.string.Pleasewritepassword)
                            }


                        } else {

                            tv_errorPhoneNumber.visibility = View.VISIBLE
                            ed_phoneNumber.background =
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ed_errorboundary
                                )
                            tv_errorPhoneNumber.text = getString(R.string.Pleasewritephonenumber)

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

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.signUpResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    // sharedViewModel.verifyOtpHoldUserName = editTextUserName.getText().toString()
                    //  sharedViewModel.isForGotVerify = false
                    //  Navigation.findNavController(mView).navigate(R.id.action_signupFragment_to_verifyOtpFragment)
                    Toast.makeText(requireContext(), "Sign Up Success", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })

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

            // fieldValidations()
            if(profileImageFile == null){

                Snackbar.make(mView, getString(R.string.Profile_pic_is_null), Snackbar.LENGTH_SHORT).show()

            }else {


                mViewModel.signUpAPICall(

                    ed_firstName.text.toString(),
                    ed_lastName.text.toString(),
                    ed_userName.text.toString(),
                    ed_phoneNumber.text.toString(),
                    profileImageFile!!,
                    ed_email.text.toString(),
                    ed_confirmPassword.text.toString(),
                    "user"
                )
            }

            /* Navigation.findNavController(btn_changePassword)
                 .navigate(R.id.action_signUpFragment_to_signupConfigurationsFragment)*/

        })
    }


}