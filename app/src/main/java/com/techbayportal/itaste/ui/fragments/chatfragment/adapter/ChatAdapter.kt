package com.techbayportal.itaste.ui.fragments.chatfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.databinding.ItemChatLeftBinding
import com.techbayportal.itaste.databinding.ItemChatRightBinding
import com.techbayportal.itaste.databinding.ItemOrderChatItemBinding
import com.techbayportal.offsidesportsapp.data.models.chat.ChatMessageDataClass
import kotlinx.android.synthetic.main.item_chat_right.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(
    private val chatArrayList: ArrayList<ChatMessageDataClass>,
    private val currentUserId: String,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolderClassLeft(val itemBinding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    inner class ViewHolderOrderClassLeft(val itemBinding: ItemOrderChatItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    inner class ViewHolderClassRight(val itemBinding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 0) {
            ViewHolderOrderClassLeft(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_order_chat_item,
                    parent,
                    false
                )
            )
        } else if (viewType == 1) {
            ViewHolderClassLeft(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_chat_left,
                    parent,
                    false
                )
            )
        } else {
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
        val chatModel = chatArrayList[position]
        when (holder) {
            is ViewHolderOrderClassLeft -> {
                if(chatModel.orderImage.isNotEmpty()){
                    Picasso.get().load(chatArrayList[chatArrayList.size -1].orderImage).fit().centerCrop()
                        .into(holder.itemBinding.ivOrderImage, object : Callback {
                            override fun onSuccess() {
                                holder.itemBinding.skOrderImage.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                Picasso.get().load(R.drawable.placeholder_image).into(holder.itemBinding.ivOrderImage)
                                holder.itemBinding.skOrderImage.visibility = View.GONE
                            }

                        })
                }

                holder.itemBinding.tvOrderId.text = "ORDER ID :: "+ chatArrayList[chatArrayList.size -1].orderId
                holder.itemBinding.tvOrderMessage.text = chatArrayList[chatArrayList.size -1].message
            }
            is ViewHolderClassLeft -> {
                holder.itemBinding.tvMessage.text = chatModel.message
                holder.itemBinding.tvMessageTime.text = getFormattedTime(chatModel.createdAt)
                if(chatModel.imgStr.isNotEmpty()){
                    Picasso.get().load(chatModel.imgStr).fit().centerCrop()
                        .into(holder.itemBinding.sivUserPicLeft, object : Callback {
                            override fun onSuccess() {
                                holder.itemBinding.skUserPicLeft.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                Picasso.get().load(R.drawable.placeholder_image).into(holder.itemBinding.sivUserPicLeft)
                                holder.itemBinding.skUserPicLeft.visibility = View.GONE
                            }

                        })
                }
                
            }
            is ViewHolderClassRight -> {
                holder.itemBinding.tvMessageRight.text = chatModel.message
                holder.itemBinding.tvMessageTime.text = getFormattedTime(chatModel.createdAt)
                if(chatModel.imgStr.isNotEmpty()){
                    Picasso.get().load(chatModel.imgStr).fit().centerCrop()
                        .into(holder.itemBinding.sivUserPicRight, object : Callback {
                            override fun onSuccess() {
                                holder.itemBinding.skUserPicRight.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                Picasso.get().load(R.drawable.placeholder_image).into(holder.itemBinding.sivUserPicRight)
                                holder.itemBinding.skUserPicRight.visibility = View.GONE
                            }

                        })
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return chatArrayList.size
    }

    fun setData(data: List<ChatMessageDataClass>) {
        chatArrayList.clear()
        chatArrayList.addAll(data)
        notifyDataSetChanged()
    }

    private fun getFormattedTime(date: Date?): String {
        return if (date == null) {
            ""
        } else {
            val sdf = SimpleDateFormat("hh:mm aaa")
            sdf.format(date)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = chatArrayList[position]
        return when {
            position == chatArrayList.size -1 -> {
                0
            }

            data.senderId == currentUserId -> {
                2
            }
            else -> {
                1
            }
        }
    }
}