package com.techbayportal.itaste.ui.fragments.cartfragment

import androidx.hilt.lifecycle.ViewModelInject
import com.techbayportal.itaste.baseclasses.BaseViewModel
import com.techbayportal.itaste.data.remote.reporitory.MainRepository
import com.techbayportal.itaste.utils.NetworkHelper

class CartViewModel @ViewModelInject constructor(private val mainRepository: MainRepository,
                                                 private val networkHelper: NetworkHelper
): BaseViewModel() {
}