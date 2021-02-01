package com.techbayportal.itaste.ui.fragments.changeexistingpasswordfragment

import android.os.Bundle
import android.text.TextUtils
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
import com.techbayportal.itaste.databinding.LayoutChangeexistingpasswordfragmentBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_changeexistingpasswordfragment.*

@AndroidEntryPoint
class ChangeExistingPasswordFragment :
    BaseFragment<LayoutChangeexistingpasswordfragmentBinding, ChangeExistingPasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changeexistingpasswordfragment
    override val viewModel: Class<ChangeExistingPasswordViewModel>
        get() = ChangeExistingPasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTextWatcher()

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()


        mViewModel.onBtnClicked.observe(this, Observer {
            validationsCheck()
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }

    private fun fieldTextWatcher() {
        ed_existingPassword.doOnTextChanged { text, start, before, count ->
            tv_errorExistingPassword.visibility = View.GONE
            ed_existingPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_newPassword.doOnTextChanged { text, start, before, count ->
            tv_newPassword.visibility = View.GONE
            ed_newPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_confirmPassword.doOnTextChanged { text, start, before, count ->
            tv_confirmPassword.visibility = View.GONE
            ed_confirmPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.changeExistingPasswordResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()


                    DialogClass.successDialog(requireContext(), getString(R.string.Password_changed_successfully), baseDarkMode)

                    ed_existingPassword.text.clear()
                    ed_newPassword.text.clear()
                    ed_confirmPassword.text.clear()

                    Navigation.findNavController(img_back).popBackStack()

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    fun validationsCheck() {
        if (!TextUtils.isEmpty(ed_existingPassword.text)) {

            if (!TextUtils.isEmpty(ed_newPassword.text)) {

                if (!TextUtils.isEmpty(ed_confirmPassword.text)) {

                    if (ed_newPassword.text.toString() == ed_confirmPassword.text.toString()) {

                        mViewModel.hitChangeExistingPassword(
                            ed_existingPassword.text.toString(),
                            ed_newPassword.text.toString(),
                            ed_confirmPassword.text.toString()
                        )

                    } else {


                        tv_confirmPassword.visibility = View.VISIBLE
                        ed_confirmPassword.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                        tv_confirmPassword.text =
                            getString(R.string.New_and_confirm_password_does_not_match)
                    }


                } else {

                    tv_confirmPassword.visibility = View.VISIBLE
                    ed_confirmPassword.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                    tv_confirmPassword.text = getString(R.string.Please_write_Confirm_Password)

                }


            } else {
                tv_newPassword.visibility = View.VISIBLE
                ed_newPassword.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                tv_newPassword.text = getString(R.string.Please_write_New_Password)

            }


        } else {
            tv_errorExistingPassword.visibility = View.VISIBLE
            ed_existingPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_errorExistingPassword.text = getString(R.string.Please_write_Existing_Password)
        }
    }


}