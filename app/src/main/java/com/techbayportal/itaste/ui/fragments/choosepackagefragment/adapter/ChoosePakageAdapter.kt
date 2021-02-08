package com.techbayportal.itaste.ui.fragments.choosepackagefragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.NotificationResponseData
import com.techbayportal.itaste.data.models.PackagesResponseData
import com.techbayportal.itaste.ui.fragments.notificationfragment.adapter.NotificationFragmentAdapter

class ChoosePakageAdapter(
    private val list: List<PackagesResponseData>,
    val context: Context,
    private val listener: ClickItemListener) :
    RecyclerView.Adapter<ChoosePakageAdapter.ViewHolder>() {

    private var rowIndex = -1

    interface ClickItemListener {
        fun onClicked(packagesResponseData: PackagesResponseData){

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        /*holder.linear!!.setOnClickListener(View.OnClickListener { v: View? ->

            rowIndex = position
            notifyDataSetChanged()
        })*/

        if (rowIndex == position) {
            holder.linear!!.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange))


            holder.ivPackageIcon!!.setColorFilter(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvPackageType!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tv_price!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tv_currency!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvWeeklyUpdates!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvCancelAnytime!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvDash!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvNoOfPosts!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvPostsPerWeek!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.tvmanualWay!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))



        } else {
            holder.linear!!.setBackgroundColor(ContextCompat.getColor(context, R.color.itemPackageBgColor))


            holder.ivPackageIcon!!.setColorFilter(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvPackageType!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tv_price!!.setTextColor(ContextCompat.getColor(context, R.color.colorOrange))
            holder.tv_currency!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvWeeklyUpdates!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvCancelAnytime!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvDash!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvNoOfPosts!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvPostsPerWeek!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))
            holder.tvmanualWay!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))



        }
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_pakages_layout,
                parent,
                false
            )
        ) {

        var ivPackageIcon : ImageView? = null
        var tvPackageType: TextView? = null
        var tv_price: TextView? = null
        var tv_currency : TextView? = null
        var tvWeeklyUpdates : TextView? = null
        var tvCancelAnytime : TextView? = null

        var tvDash : TextView? = null
        var tvNoOfPosts: TextView? = null
        var tvPostsPerWeek : TextView? = null

        var tvmanualWay : TextView? = null



        var linear: LinearLayout? = null




        init {
            tv_price = itemView.findViewById(R.id.tv_price)
            tvPackageType = itemView.findViewById(R.id.tv_packageType)
            tvNoOfPosts = itemView.findViewById(R.id.tv_noOfPosts)
            linear = itemView.findViewById(R.id.linear_back)
            tv_currency = itemView.findViewById(R.id.tv_currency)
            ivPackageIcon = itemView.findViewById(R.id.iv_packageTypeIcon)


            tvWeeklyUpdates = itemView.findViewById(R.id.tv_weekelyUpdates)
            tvCancelAnytime = itemView.findViewById(R.id.tv_cancelAnytime)
            tvDash = itemView.findViewById(R.id.tv_dash)
            tvPostsPerWeek = itemView.findViewById(R.id.tv_postsPerWeek)
            tvmanualWay = itemView.findViewById(R.id.tv_manual_way)

        }

        fun bind(position: Int) {
            val model = list[position]
            tvPackageType!!.text = model.name
            tv_price!!.text = model.price.toString()

            tvWeeklyUpdates!!.text = "- " + model.weekly_update
            tvCancelAnytime!!.text = "- "+model.cancelation_time

            tvPostsPerWeek!!.text = model.post_week
            tvmanualWay!!.text = "- "+model.change_subscription

            itemView.setOnClickListener {
                listener.onClicked(model)
                rowIndex = position
                notifyDataSetChanged()
            }


        }

    }

}