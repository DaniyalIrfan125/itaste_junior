package com.techbayportal.itaste.ui.fragments.notification.adapter

import android.app.Notification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemBlockedAccountsUsersBinding
import com.techbayportal.itaste.databinding.ItemNotificationBinding
import com.techbayportal.itaste.ui.fragments.blockedaccounts1.adapter.BlockedAccounts1Adapter
import com.techbayportal.itaste.ui.fragments.notification.itemclicklistener.NotificationRvItemClickListener

class NotificationAdapter(val onClickListener: NotificationRvItemClickListener): RecyclerView.Adapter<NotificationAdapter.ViewHolderClass>() {

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