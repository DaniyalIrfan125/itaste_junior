package com.techbayportal.itaste.ui.fragments.notificationfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.NotificationResponseData
import com.techbayportal.itaste.ui.fragments.notificationfragment.itemclicklistener.NotificationRvItemClickListener
import kotlinx.android.synthetic.main.item_notification.*
import timber.log.Timber
import java.lang.Exception

class NotificationFragmentAdapter(
    val context: Context,
    private val list: ArrayList<NotificationResponseData>,
    private val listener: ClickItemListener
) : RecyclerView.Adapter<NotificationFragmentAdapter.ViewHolder>() {

    var rowIndex = -1

    interface ClickItemListener {
        fun onClicked(notificationResponseData: NotificationResponseData)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationFragmentAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: NotificationFragmentAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_notification, parent, false)) {

        private var rlNotificationItem: RelativeLayout? = null
        private var tvNotifText: TextView? = null
        private var ivUserDp: ImageView? = null
        private var ivPostImage: ImageView? = null
        private var skLeft: SpinKitView? = null
        private var skRight: SpinKitView? = null

        init {
            tvNotifText = itemView.findViewById(R.id.tv_notification_text)
            rlNotificationItem = itemView.findViewById(R.id.rl_notification_item)
            ivUserDp = itemView.findViewById(R.id.siv_notification_user_image)
            ivPostImage = itemView.findViewById(R.id.iv_notification_post)
            skLeft = itemView.findViewById(R.id.sk_notification_left)
            skRight = itemView.findViewById(R.id.sk_notification_right)
        }

        fun bind(position: Int) {
            val model = list[position]
            tvNotifText!!.text = model.heading

            if(model.image_left.isNotEmpty()){
                Picasso.get()
                    .load(model.image_left).fit().centerCrop()
                    .into(ivUserDp, object : Callback {
                        override fun onSuccess() {
                            Timber.d("on success User Location Image")
                            skLeft!!.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image).into(ivUserDp)
                            skLeft!!.visibility = View.GONE
                            Timber.d("on error User Location Image: $e")
                        }

                    })
            }

            if(model.image_right.isNotEmpty()){
                Picasso.get()
                    .load(model.image_right).fit().centerCrop()
                    .into(ivPostImage, object : Callback {
                        override fun onSuccess() {
                            Timber.d("on success User Location Image")
                            skRight!!.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image).into(ivPostImage)
                            skRight!!.visibility = View.GONE
                            Timber.d("on error User Location Image: $e")
                        }

                    })
            }


            itemView.setOnClickListener {
                listener.onClicked(model)
                rowIndex = position
                notifyDataSetChanged()
            }

            if (rowIndex == position) {
                rlNotificationItem!!.setBackgroundColor(ContextCompat.getColor(context, R.color.bottomNavigationColor))

            } else {
                rlNotificationItem!!.setBackgroundColor(ContextCompat.getColor(context, R.color.bottomNavigationColor));

            }

        }


    }
}