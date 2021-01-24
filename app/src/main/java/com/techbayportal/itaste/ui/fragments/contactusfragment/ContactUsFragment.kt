package com.techbayportal.itaste.ui.fragments.contactusfragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentContactUsBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_contact_us.img_back
import kotlinx.android.synthetic.main.fragment_contact_us.tv_errorUsername
import kotlinx.android.synthetic.main.fragment_contact_us.tv_error_userEmail
import kotlinx.android.synthetic.main.fragment_report_bug.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.layout_signupfragment.*


@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding, ContactUsViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_contact_us
    override val viewModel: Class<ContactUsViewModel>
        get() = ContactUsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        fieldTextWatcher()
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.contactUsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    Navigation.findNavController(mView).popBackStack()
                    //Snackbar.make(requireView(), it.data!!.message, Snackbar.LENGTH_SHORT).show()


                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    private fun validateFields() {
        if (et_userName.text.isNotEmpty()) {
            if (et_userEmail.text.isNotEmpty()) {
                if (Patterns.EMAIL_ADDRESS.matcher(et_userEmail.text.toString()).matches()) {
                    if (et_usermessage.text.isNotEmpty()) {
                        mViewModel.hitContactUs(
                            et_userName.text.toString(),
                            et_userEmail.text.toString(),
                            et_usermessage.text.toString()
                        )
                    } else {
                        tv_errorUserMessage.visibility = View.VISIBLE
                        et_usermessage.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ed_errorboundary_bug
                        )
                        tv_errorUserMessage.text = getString(R.string.writereport)
                    }
                } else {
                    tv_error_userEmail.text = getString(R.string.writevalidemail)
                    tv_error_userEmail.visibility = View.VISIBLE
                }
            } else {
                tv_error_userEmail.text = getString(R.string.PleasewriteEmail)
                tv_error_userEmail.visibility = View.VISIBLE
            }
        } else {
            tv_errorUsername.visibility = View.VISIBLE
            // et_userName.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary_bug)
            tv_errorUsername.text = getString(R.string.writename)
        }
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onConfirmButtonClicked.observe(this, Observer {
            validateFields()
        })


        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }

    private fun fieldTextWatcher() {

        et_userName.doOnTextChanged { text, start, before, count ->
            tv_errorUsername.visibility = View.GONE
            //et_userName.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_states_bug)
        }

        et_userEmail.doOnTextChanged { text, start, before, count ->
            tv_error_userEmail.visibility = View.GONE
            //et_userEmail.background = ContextCompat.getDrawable(requireContext(), R.drawable.ed_states_bug)
        }

        et_usermessage.doOnTextChanged { text, start, before, count ->
            tv_errorUserMessage.visibility = View.GONE
            et_usermessage.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states_bug)
        }
    }


}