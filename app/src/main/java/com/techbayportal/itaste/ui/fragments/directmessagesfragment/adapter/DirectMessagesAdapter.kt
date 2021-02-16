package com.techbayportal.itaste.ui.fragments.directmessagesfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemMessageListItemBinding
import com.techbayportal.itaste.ui.fragments.directmessagesfragment.itemClickListener.DirectMessagesRvItemClickListener
import com.techbayportal.offsidesportsapp.data.models.chat.GeneralInboxDataClass
import java.lang.Exception

class DirectMessagesAdapter (
    private val list: ArrayList<GeneralInboxDataClass>,
    val listener: ClickListener
    //val onClickListener: DirectMessagesRvItemClickListener
): RecyclerView.Adapter<DirectMessagesAdapter.ViewHolderClass>() {

    interface ClickListener {
        fun onClick(data: GeneralInboxDataClass)
    }

    inner class ViewHolderClass(private val itemBinding: ItemMessageListItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(data: GeneralInboxDataClass) {

            itemBinding.rlInboxItem .setOnClickListener {
                listener.onClick(data)
            }
            if(data.imgStr .isNotEmpty()){
                Picasso.get().load(data.imgStr).fit().centerCrop()
                    .into(itemBinding.sivUserImage, object : Callback {
                        override fun onSuccess() {
                            itemBinding.skInboxImage.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image).into(itemBinding.sivUserImage)
                            itemBinding.skInboxImage.visibility = View.GONE
                        }

                    })
            }
           // itemBinding.sivUserImage.loadPerson(data.imgStr)
            itemBinding.tvUserName.text = data.senderName
            /*update this with number of unread messages*/
            itemBinding.noOfMessages.text = data.unreadMessages.toString() + "Unread Messages"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        return ViewHolderClass(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_message_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}