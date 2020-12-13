package com.techbayportal.itaste.ui.fragments.choosepackagefragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R

class ChoosePakageAdapter(private val list: List<Int>, val context: Context) :
    RecyclerView.Adapter<ChoosePakageAdapter.ViewHolder>() {

    private var row_index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        holder.linear!!.setOnClickListener(View.OnClickListener { v: View? ->

            row_index = position
            notifyDataSetChanged()
        })

        if (row_index == position) {
            holder.linear!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorOrange
                )
            )


            holder.tv_price!!.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
        } else {
            holder.linear!!.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorGreyBackground
                )
            )
            holder.tv_price!!.setTextColor(ContextCompat.getColor(context, R.color.colorOrange))
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


        var tv_price: TextView? = null
        var linear: LinearLayout? = null

        init {
            tv_price = itemView.findViewById(R.id.tv_price)
            linear = itemView.findViewById(R.id.linear_back)

        }

        fun bind(int: Int) {


        }

    }

}