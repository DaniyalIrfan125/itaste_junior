package com.techbayportal.itaste.ui.fragments.chatfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemChatLeftBinding
import com.techbayportal.itaste.databinding.ItemChatRightBinding

class ChatAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolderClassLeft(private val itemBinding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    inner class ViewHolderClassRight(private val itemBinding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType==1) {
            ViewHolderClassLeft(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chat_left,
                    parent,
                    false
                )
            )
        } else{
            ViewHolderClassRight(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chat_right,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0)
            1
        else 2
    }
}