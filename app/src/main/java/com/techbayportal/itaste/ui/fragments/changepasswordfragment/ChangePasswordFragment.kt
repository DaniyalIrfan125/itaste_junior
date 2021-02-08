package com.techbayportal.itaste.ui.fragments.changepasswordfragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.local.datastore.DataStoreProvider
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutChangepasswordfragmentBinding
import com.techbayportal.itaste.ui.activities.mainactivity.MainActivity
import com.techbayportal.itaste.ui.activities.signupactivity.SignupActivity
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_changepasswordfragment.ed_confirmPassword
import kotlinx.android.synthetic.main.layout_changepasswordfragment.tv_errorConfirmPassword
import kotlinx.android.synthetic.main.layout_loginfragment.*
import kotlinx.android.synthetic.main.layout_signupfragment.*

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<LayoutChangepasswordfragmentBinding, ChangePasswordViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_changepasswordfragment
    override val viewModel: Class<ChangePasswordViewModel>
        get() = ChangePasswordViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var dataStoreProvider: DataStoreProvider
    var darkMode :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldTextWatcher()
        dataStoreProvider = DataStoreProvider(requireContext())

        dataStoreProvider.darkModeFlow.asLiveData().observe(this, Observer {
            if (it) {
                darkMode

            } else {
                !darkMode
            }
        })
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


                   // navigateToLoginScreen()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode )
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
       if(!TextUtils.isEmpty(ed_newPassword.text) && ed_newPassword.text.toString().length > 5){

           if(!TextUtils.isEmpty(ed_confirmPassword.text) && ed_newPassword.text.toString().length > 5){

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
               if(ed_confirmPassword.text.toString().length in 1..5){
                   tv_errorConfirmPassword.text = getString(R.string.password_at_least_six_character)
               }else{
                   tv_errorConfirmPassword.text = getString(R.string.Pleasewritepassword)
               }
               tv_errorConfirmPassword.visibility = View.VISIBLE
               ed_confirmPassword.background =
                   ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)

           }

       }
        else{
           if(ed_newPassword.text.toString().length in 1..5){
               tv_errorNewPassword.text = getString(R.string.password_at_least_six_character)
           }else{
               tv_errorNewPassword.text = getString(R.string.Pleasewritepassword)
           }
           tv_errorNewPassword.visibility = View.VISIBLE
           ed_newPassword.background =
               ContextCompat.getDrawable(requireContext(), R.drawable.ed_errorboundary)


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