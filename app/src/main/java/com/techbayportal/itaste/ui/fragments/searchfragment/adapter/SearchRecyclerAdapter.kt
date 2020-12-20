package com.techbayportal.itaste.ui.fragments.searchfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemSearchDoubleImageBinding
import com.techbayportal.itaste.databinding.ItemSearchThreeImageBinding


class SearchRecyclerAdapter(private val list: List<Int>, val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderClassDouble(val itemBinding: ItemSearchDoubleImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    inner class ViewHolderClassTripple(val itemBinding: ItemSearchThreeImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==1) {
            ViewHolderClassDouble(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_double_image,
                    parent,
                    false
                )
            )
        } else{
            ViewHolderClassTripple(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_three_image,
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0)
            1
        else 2
    }

}