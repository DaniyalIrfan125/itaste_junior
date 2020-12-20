package com.techbayportal.itaste.ui.fragments.blockedaccounts1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemBlockedAccountsUsersBinding
import com.techbayportal.itaste.ui.fragments.blockedaccounts1.itemclicklistener.BlockedAccounts1RvClickListener

class BlockedAccounts1Adapter(val onClickListener: BlockedAccounts1RvClickListener): RecyclerView.Adapter<BlockedAccounts1Adapter.ViewHolderClass>() {

    inner class ViewHolderClass(val itemBinding:ItemBlockedAccountsUsersBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData() {
            itemBinding.root.setOnClickListener {
                onClickListener.onItemClickListener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_blocked_accounts_users,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 5
    }
}