package com.techbayportal.itaste.ui.fragments.myprofilefragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent

class MyProfileViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onEditProfileClicked = SingleLiveEvent<Any>()
    var onMyCartClicked = SingleLiveEvent<Any>()
    var onChangePasswordClicked = SingleLiveEvent<Any>()
    val onSavedPostsClicked = SingleLiveEvent<Any>()
    val onReportBugClicked = SingleLiveEvent<Any>()
    val onDeleteAccountClicked = SingleLiveEvent<Any>()
    val onBlockedAccountClicked = SingleLiveEvent<Any>()
    val onLogoutClicked = SingleLiveEvent<Any>()


    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }

    fun onEditProfileClicked(){
        onEditProfileClicked.call()
    }

    fun onMyCartClicked(){
        onMyCartClicked.call()
    }

    fun onChangePasswordClicked(){
        onChangePasswordClicked.call()
    }

    fun onSavedPostsClicked(){
        onSavedPostsClicked.call()
    }

    fun onReportBugClicked(){
        onReportBugClicked.call()
    }

    fun onDeleteAccountClicked(){
        onDeleteAccountClicked.call()
    }

    fun onBlockedAccountClicked(){
        onBlockedAccountClicked.call()
    }

    fun onLogoutClicked(){
        onLogoutClicked.call()
    }

}