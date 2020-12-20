package com.techbayportal.itaste.ui.fragments.notificationfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemNotificationBinding
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener

class NotificationFragmentAdapter(val onClickListener: NotificationRvItemClickListener): RecyclerView.Adapter<NotificationFragmentAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(val itemBinding: ItemNotificationBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData() {
            itemBinding.root.setOnClickListener {
                onClickListener.onItemClickListener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notification,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 5
    }
}