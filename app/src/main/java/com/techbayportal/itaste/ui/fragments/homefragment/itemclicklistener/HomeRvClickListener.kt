package com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener

import com.techbayportal.itaste.ui.fragments.homefragment.HomeFragment

interface HomeRvClickListener {
    fun onItemClickListener(type: String)
    fun onChildItemClick(position: Int)
}