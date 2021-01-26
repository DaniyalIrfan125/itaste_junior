package com.techbayportal.itaste.ui.fragments.postfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetTimeSuggestionData
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeChildRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeRecyclerAdapter

class TimeDurationAdapter(
    private val list: ArrayList<GetTimeSuggestionData>,
    val context: Context
) :
    RecyclerView.Adapter<TimeDurationAdapter.ViewHolder>() {

    private var onClickListener: TimeDurationItemClickListener? = null

    interface TimeDurationItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnEntryClickListener(onClickListener: TimeDurationItemClickListener?) {
        this.onClickListener = onClickListener
    }


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
        private var relative_main: RelativeLayout ? = null


        init {
            text_time = itemView.findViewById(R.id.text_time)
            relative_main = itemView.findViewById(R.id.relative_main)
        }

        fun bind(int: Int) {
            text_time!!.text = list[int].time

            relative_main?.setOnClickListener {
                onClickListener?.onItemClick(int)
            }

        }

    }

}