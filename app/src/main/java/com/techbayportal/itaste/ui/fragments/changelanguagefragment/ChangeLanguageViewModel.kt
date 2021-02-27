package com.techbayportal.itaste.ui.fragments.changelanguagefragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.navigation.Navigation
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper
import com.techbayportal.itaste.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.layout_cartfragment.*

class ChangeLanguageViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    val onBackButtonClicked = SingleLiveEvent<Any>()
    val onSaveButtonClicked = SingleLiveEvent<Any>()

    fun onBackButtonClicked() {
        onBackButtonClicked.call()
    }
    fun onSaveButtonClicked() {
        onSaveButtonClicked.call()
    }



}