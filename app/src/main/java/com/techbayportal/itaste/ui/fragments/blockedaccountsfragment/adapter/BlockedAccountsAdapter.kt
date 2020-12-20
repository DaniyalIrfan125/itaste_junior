package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemBlockedAccountsBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccountsRvClickListener

class BlockedAccountsAdapter(val onClickListener: BlockedAccountsRvClickListener): RecyclerView.Adapter<BlockedAccountsAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(private val itemBinding: ItemBlockedAccountsBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData() {
            itemBinding.root.setOnClickListener {
                onClickListener.onItemClickListener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_blocked_accounts,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 5
    }
}