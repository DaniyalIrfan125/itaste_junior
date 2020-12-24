package com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.itemclicklistener.BlockedAccountsRvClickListener


class BlockedAccountsAdapter(val context: Context) : RecyclerView.Adapter<BlockedAccountsAdapter.ViewHolder>() {

    private var onClickListener: BlockedAccountsRvClickListener? = null

    fun setOnEntryClickListener(onEntryClickListener: BlockedAccountsRvClickListener?) {
        onClickListener = onEntryClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedAccountsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: BlockedAccountsAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_blocked_accounts,
                parent,
                false
            )
        ) {

        private var btnUnblock: Button? = null

        init {
            btnUnblock = itemView.findViewById(R.id.btn_unblock)
        }

        fun bind(int: Int) {
            btnUnblock?.setOnClickListener {
                onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.BLOCKED_ACCOUNT_RV_UNBLOCK_BUTTON)
            }
        }



    }
}