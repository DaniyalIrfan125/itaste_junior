package com.techbayportal.itaste.ui.fragments.changeexistingpasswordfragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutChangeexistingpasswordfragmentBinding
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldTextWatcher()

    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()


        mViewModel.onBtnClicked.observe(this,  Observer{
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

    fun validationsCheck() {
        if (!TextUtils.isEmpty(ed_existingPassword.text)) {

            if (!TextUtils.isEmpty(ed_newPassword.text)) {

                if (!TextUtils.isEmpty(ed_confirmPassword.text)) {

                    if (ed_newPassword.text == ed_confirmPassword.text) {



                    } else {


                        tv_confirmPassword.visibility = View.VISIBLE
                        ed_confirmPassword.background =
                            ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                        tv_confirmPassword.text = "New and confirm password doesnot match!"
                    }


                } else {

                    tv_confirmPassword.visibility = View.VISIBLE
                    ed_confirmPassword.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                    tv_confirmPassword.text = "Please write Confirm Password!"

                }


            } else {
                tv_newPassword.visibility = View.VISIBLE
                ed_newPassword.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                tv_newPassword.text = "Please write Existing Password!"

            }


        } else {
            tv_errorExistingPassword.visibility = View.VISIBLE
            ed_existingPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
            tv_errorExistingPassword.text = "Please write Existing Password!"
        }
    }


}