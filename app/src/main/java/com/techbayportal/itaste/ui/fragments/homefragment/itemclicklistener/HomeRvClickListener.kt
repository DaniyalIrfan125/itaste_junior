package com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener

import com.techbayportal.itaste.data.models.GetHomeScreenData
import com.techbayportal.itaste.ui.fragments.homefragment.HomeFragment

interface HomeRvClickListener {
   // fun onItemClickListener(type: String,id: Int)
   fun onItemClickListener(type: String, getHomeScreenData: GetHomeScreenData)
    fun onChildItemClick(position: Int, vendorId : Int)
}