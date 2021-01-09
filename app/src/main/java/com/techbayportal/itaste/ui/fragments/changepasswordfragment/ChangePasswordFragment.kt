package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutChangepasswordfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_loginfragment.*

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<LayoutChangepasswordfragmentBinding, ChangePasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changepasswordfragment
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldTextWatcher()
    }

    //navigate to home screen
    private fun navigateToLoginScreen() {
        val intent = Intent(activity, SignupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()
        mViewModel.updatePassword.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                  //  successMessageSlider.show()
                   // relative_updatePassword.visibility = View.GONE
//                    Navigation.findNavController(mView).navigate(R.id.action_forgotFragment_to_verifyOtpFragment)

                    navigateToLoginScreen()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!)
                }
            }
        })
    }

    private fun fieldTextWatcher() {
        ed_newPassword.doOnTextChanged { text, start, before, count ->
            tv_errorNewPassword.visibility = View.GONE
            ed_newPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }

        ed_confirmPassword.doOnTextChanged { text, start, before, count ->
            tv_errorConfirmPassword.visibility = View.GONE
            ed_confirmPassword.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ed_states)
        }
    }




    fun fieldValidations() {
       if(!TextUtils.isEmpty(ed_newPassword.text)){

           if(!TextUtils.isEmpty(ed_confirmPassword.text)){


               if(ed_newPassword.text.toString() == ed_confirmPassword.text.toString()){
                   mViewModel.hitUpdatePassword(sharedViewModel.verifyOtpHoldPhoneNumber,ed_newPassword.text.toString(),ed_confirmPassword.text.toString())
               }
               else{

                   tv_errorConfirmPassword.visibility = View.VISIBLE
                   ed_confirmPassword.background =
                       ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
                   tv_errorConfirmPassword.text = getString(R.string.Passworddoesnotmatch)

               }

           }
           else{
               tv_errorConfirmPassword.visibility = View.VISIBLE
               ed_confirmPassword.background =
                   ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
               tv_errorConfirmPassword.text = getString(R.string.Pleasewritepassword)
           }

       }
        else{
           tv_errorNewPassword.visibility = View.VISIBLE
           ed_newPassword.background =
               ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)
           tv_errorNewPassword.text = getString(R.string.Pleasewritepassword)

        }
    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {

            Navigation.findNavController(btn_changePassword)
                .popBackStack()
        })

        mViewModel.onSaveButtonClicked.observe(this, Observer{
            fieldValidations()
        })
    }


}