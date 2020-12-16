package com.techbayportal.itaste.ui.fragments.postfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R

class LocationAdapter (private val list: List<String>, val context: Context) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_timeduration,
                parent,
                false
            )
        ) {

        private var text_time: TextView? = null


        init {
            text_time = itemView.findViewById(R.id.text_time)

        }

        fun bind(int: Int) {
            text_time!!.text = list[int]

        }

    }

}