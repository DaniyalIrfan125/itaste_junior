package com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.DaysOfWeek

class DaysRecyclerAdapter(private val list: List<DaysOfWeek>, val context: Context) :
    RecyclerView.Adapter<DaysRecyclerAdapter.ListViewHolder>() {

    private var row_index = 0

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var tracker: SelectionTracker<DaysOfWeek>? = null

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.tv_day)
        private val dayLayout: RelativeLayout = itemView.findViewById(R.id.relative)
//        private val container: LinearLayout = itemView.findViewById(R.id.linear_layout_container)

        fun setPostImage(postItem: DaysOfWeek, isActivated: Boolean = false) {
            dayTextView.text = postItem.dayName
           // dayLayout.background = ContextCompat.getDrawable(context, R.drawable.days_of_week_background)
            itemView.isActivated = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<DaysOfWeek> =
            object : ItemDetailsLookup.ItemDetails<DaysOfWeek>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): DaysOfWeek? = list[position]
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = inflater.inflate(R.layout.item_recycler_days, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        tracker?.let {
            holder.setPostImage(list[position], it.isSelected(list[position]))
        }


        //holder.bind(position)

        /*holder.relative!!.setOnClickListener(View.OnClickListener { v: View? ->

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

        }*/
    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int) = list[position]
    fun getPosition(name: String) = list.indexOfFirst { it.dayName == name }


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

            tv_day!!.text = list[position].dayName

        }

    }

}

