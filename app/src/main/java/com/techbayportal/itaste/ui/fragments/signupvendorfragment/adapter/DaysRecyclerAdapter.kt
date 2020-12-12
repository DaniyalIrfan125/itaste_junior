package com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R

class DaysRecyclerAdapter(private val list: List<String>, val context: Context) :
    RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder>() {

    private var row_index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        holder.relative!!.setOnClickListener(View.OnClickListener { v: View? ->

            row_index = position
            notifyDataSetChanged()
        })

        if (row_index == position) {
            holder.relative!!.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.item_days_orange
                )
            )

        } else {
            holder.relative!!.setBackground(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.item_days_grey_boundary
                )
            )

        }
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_recycler_days,
                parent,
                false
            )
        ) {

         var relative: RelativeLayout? = null
         var tv_day: TextView? = null

        init {
            relative = itemView.findViewById(R.id.relative)
            tv_day = itemView.findViewById(R.id.tv_day)

        }

        fun bind(position: Int) {

            tv_day!!.text = list[position]

        }

    }

}

