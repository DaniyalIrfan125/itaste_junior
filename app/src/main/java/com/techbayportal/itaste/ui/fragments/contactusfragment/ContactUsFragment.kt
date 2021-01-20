package com.techbayportal.itaste.ui.fragments.contactusfragment

import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.first_fragment.*
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_delete_account.*
import kotlinx.android.synthetic.main.fragment_delete_account.img_back
import kotlinx.android.synthetic.main.fragment_vendor_profile.*

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding, ContactUsViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_contact_us
    override val viewModel: Class<ContactUsViewModel>
        get() = ContactUsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    //Navigation.findNavController(img_back).popBackStack()
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT)
                        .show()


                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    /*private fun validateFields() {
        if (et_vendorFirstName.text.isNotEmpty()) {
            if (et_vendorLastName.text.isNotEmpty()) {
                if (et_vendorBio.text.isNotEmpty()) {
                }
            }
        }
    }*/

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onConfirmButtonClicked.observe(this, Observer {
            mViewModel.hitContactUs(
                et_userName.text.toString(),
                et_userEmail.text.toString(),
                et_usermessage.text.toString()
            )
        })


        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


}