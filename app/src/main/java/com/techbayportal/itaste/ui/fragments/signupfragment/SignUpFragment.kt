package com.techbayportal.itaste.ui.fragments.signupfragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSignupfragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_loginfragment.*
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class SignUpFragment : BaseFragment<LayoutSignupfragmentBinding, SignUpFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_signupfragment
    override val viewModel: Class<SignUpFragmentViewModel>
        get() = SignUpFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTextWatcher()
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


                                    } else {

                                        tv_errorConfirmPassword.visibility = View.VISIBLE
                                        ed_confirmPassword.background =
                                            ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.ed_errorboundary
                                            )
                                        tv_errorConfirmPassword.text = "Passwords doesnot match!"

                                    }


                                } else {

                                    tv_errorConfirmPassword.visibility = View.VISIBLE
                                    ed_confirmPassword.background =
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.ed_errorboundary
                                        )
                                    tv_errorConfirmPassword.text = "Please write confirm password!"
                                }


                            } else {

                                tv_errorSetPassword.visibility = View.VISIBLE
                                ed_setPassword.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ed_errorboundary
                                    )
                                tv_errorSetPassword.text = "Please write password!"
                            }


                        } else {

                            tv_errorPhoneNumber.visibility = View.VISIBLE
                            ed_phoneNumber.background =
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.ed_errorboundary
                                )
                            tv_errorPhoneNumber.text = "Please write phone number!"

                        }

                    } else {

                        tv_errorEmail.visibility = View.VISIBLE
                        ed_email.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                        tv_errorEmail.text = "Please write Email!"


                    }


                } else {

                    tv_errorUsername.visibility = View.VISIBLE
                    ed_userName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                    tv_errorUsername.text = "Please write Username!"

                }


            } else {
                tv_errorName.visibility = View.VISIBLE
                ed_lastName.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                tv_errorName.text = "Please write Last Name!"
            }

        } else {
            tv_errorName.visibility = View.VISIBLE
            ed_firstName.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_errorName.text = "Please write First Name!"
        }
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onSignUpButtonClicked.observe(this, Observer {

            fieldValidations()

//            Navigation.findNavController(btn_changePassword)
//                .navigate(R.id.action_signUpFragment_to_signupConfigurationsFragment)
//
        })
    }


}