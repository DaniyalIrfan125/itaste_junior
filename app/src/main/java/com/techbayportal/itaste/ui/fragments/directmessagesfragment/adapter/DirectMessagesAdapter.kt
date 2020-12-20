package com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemMessageListItemBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.itemClickListener.DirectMessagesRvItemClickListener

class DirectMessagesAdapter (val onClickListener: DirectMessagesRvItemClickListener): RecyclerView.Adapter<DirectMessagesAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(private val itemBinding: ItemMessageListItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData() {
            itemBinding.root.setOnClickListener {
                onClickListener.onItemClickListener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_message_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 15
    }
}