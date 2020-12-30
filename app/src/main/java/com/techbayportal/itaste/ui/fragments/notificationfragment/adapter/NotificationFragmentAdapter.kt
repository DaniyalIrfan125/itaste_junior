package com.techbayportal.itaste.ui.fragments.notificationfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener

class NotificationFragmentAdapter(val context: Context): RecyclerView.Adapter<NotificationFragmentAdapter.ViewHolder>() {

    private var onClickListener: NotificationRvItemClickListener? = null

    fun setOnEntryClickListener(onEntryClickListener: NotificationRvItemClickListener?) {
        onClickListener = onEntryClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationFragmentAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NotificationFragmentAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_notification,
                parent,
                false
            )
        ) {

        private var rvNotificationItem: RelativeLayout? = null

        init {
            rvNotificationItem = itemView.findViewById(R.id.rvNotificationItem)
        }

        fun bind(int: Int) {
            rvNotificationItem?.setOnClickListener {
                onClickListener?.onItemClickListener(AppConstants.RecyclerViewKeys.NOTIFICATION_ITEM)
            }
        }



    }
}