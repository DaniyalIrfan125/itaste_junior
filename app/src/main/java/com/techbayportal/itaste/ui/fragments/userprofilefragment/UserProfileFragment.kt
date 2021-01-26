package com.techbayportal.itaste.ui.fragments.userprofilefragment

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.os.FileUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.opensooq.supernova.gligar.GligarPicker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.UserPersonalProfileResponseData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentUserProfileBinding
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.et_country_code
import kotlinx.android.synthetic.main.fragment_user_profile.img_back
import kotlinx.android.synthetic.main.fragment_user_profile.tv_userName
import kotlinx.android.synthetic.main.fragment_vendor_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.echodev.resizer.Resizer
import okhttp3.MultipartBody
import java.io.File
import java.nio.file.Files.size

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding, UserProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_profile
    override val viewModel: Class<UserProfileViewModel>
        get() = UserProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var userPersonalProfileResponseData: UserPersonalProfileResponseData
    var profileImageFile: File? = null
    private lateinit var mView: View
    var compressedProfileImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
        mViewModel.hitGetUserPersonalProfile()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldTextWatcher()
        mView = view
        if (this::userPersonalProfileResponseData.isInitialized) {
            setData(userPersonalProfileResponseData)
        }

        et_country_code.registerPhoneNumberTextView(et_userPhone)
        et_userPhone.hint = "5472227999"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            if (requestCode == AppConstants.USER_PROFILE_PIC_CODE) {
                val imagePath = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)!![0]
                profileImageFile = File(imagePath)

                if (profileImageFile != null) {
                    //calling method to compress image
                    compressImageFile(profileImageFile)
                    Picasso.get()
                        .load(profileImageFile!!)
                        .fit()
                        .centerCrop()
                        .into(siv_userProfilePic)
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

        mViewModel.getUserPersonalPersonalResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    userPersonalProfileResponseData = it.data!!.data
                    setData(userPersonalProfileResponseData)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.getUpdateUserPersonalProfileResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    //hit api again to get updated data

                    if (et_country_code.fullNumberWithPlus != userPersonalProfileResponseData.phone) {
                        sharedViewModel.isUpdatePhone = true
                        sharedViewModel.isForGotVerify = false
                        sharedViewModel.verifyOtpHoldNewPhoneNumber =
                            et_country_code.fullNumberWithPlus
                        Navigation.findNavController(mView)
                            .navigate(R.id.action_userProfileFragment_to_otpverificationFragment2)

                    } else {
                        mViewModel.hitGetUserPersonalProfile()
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
        if (et_userFirstName.text.isNotEmpty()) {
            if (et_userLastName.text.isNotEmpty()) {
                if (et_userUserEmail.text.isNotEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(et_userUserEmail.text.toString())
                            .matches()
                    ) {
                        if (et_userPhone.text.isNotEmpty()) {
                            /*if (et_country_code.fullNumberWithPlus == userPersonalProfileResponseData.phone) {*/
                            if (profileImageFile != null) {
                                if (compressedProfileImageFile != null) {
                                    mViewModel.hitUpdateUserPersonalProfile(
                                        et_userFirstName.text.toString(),
                                        et_userLastName.text.toString(),
                                        et_userUserEmail.text.toString(),
                                        et_country_code.fullNumberWithPlus,
                                        compressedProfileImageFile!!)
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "File compressing...Try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                mViewModel.hitUpdateUserPersonalProfile(
                                    et_userFirstName.text.toString(),
                                    et_userLastName.text.toString(),
                                    et_userUserEmail.text.toString(),
                                    et_country_code.fullNumberWithPlus,
                                    null)
                            }
                        } else {
                            tv_error_userUserPhone.text = getString(R.string.writephone)
                            tv_error_userUserPhone.visibility = View.VISIBLE
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
                tv_error_userLastName.text = getString(R.string.writelastname)
                tv_error_userLastName.visibility = View.VISIBLE
            }
        } else {
            tv_error_userFirstName.text = getString(R.string.writefirstname)
            tv_error_userFirstName.visibility = View.VISIBLE
        }
    }

    private fun fieldTextWatcher() {

        et_userFirstName.doOnTextChanged { text, start, before, count ->
            tv_error_userFirstName.visibility = View.GONE
            //et_vendorFirstName.setTextColor(resources.getColor(R.color.colorErrorRed))
        }

        et_userLastName.doOnTextChanged { text, start, before, count ->
            tv_error_userLastName.visibility = View.GONE
        }
        et_userUserEmail.doOnTextChanged { text, start, before, count ->
            tv_error_userEmail.visibility = View.GONE
        }

        et_userPhone.doOnTextChanged { text, start, before, count ->
            tv_error_userUserPhone.visibility = View.GONE
        }
    }


    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onSaveChangesButtonClicked.observe(this, Observer {
            validateFields()
        })

        mViewModel.onProfilePicChangeTextClicked.observe(this, Observer {
            GligarPicker().requestCode(AppConstants.USER_PROFILE_PIC_CODE)
                .withFragment(this)
                .limit(1)
                .show()
        })
    }

    private fun setData(userPersonalProfileResponseData: UserPersonalProfileResponseData) {
        try {
            tv_userName.text =
                "${userPersonalProfileResponseData.first_name} ${userPersonalProfileResponseData.last_name}"
            et_userFirstName.setText(userPersonalProfileResponseData.first_name)
            et_userLastName.setText(userPersonalProfileResponseData.last_name)
            et_country_code.fullNumber = userPersonalProfileResponseData.phone
            sharedViewModel.verifyOtpHoldPhoneNumber = userPersonalProfileResponseData.phone
            et_userUserEmail.setText(userPersonalProfileResponseData.email)

                Picasso.get()
                    .load(userPersonalProfileResponseData.profile_pic).fit().centerCrop()
                    .into(siv_userProfilePic ,object :Callback{
                        override fun onSuccess() {
                            if(sk_userProfile != null) {
                                sk_userProfile.visibility = View.GONE
                            }
                        }

                        override fun onError(e: java.lang.Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image).into(siv_userProfilePic)
                            if(sk_userProfile != null) {
                                sk_userProfile.visibility = View.GONE
                            }
                        }

                    })


        } catch (e: Exception) {
        }

    }

}