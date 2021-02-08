package com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener

import com.techbayportal.itaste.ui.fragments.homefragment.HomeFragment

interface HomeRvClickListener {
    fun onItemClickListener(type: String,id: Int)
    fun onChildItemClick(position: Int)
}