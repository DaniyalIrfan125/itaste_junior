package com.techbayportal.itaste.ui.fragments.homefragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.R
import kotlinx.android.synthetic.main.layout_homefragment.*

class HomeRecyclerAdapter(private val list: List<Int>, val context: Context) :
    RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

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
                R.layout.item_home_recyclerview,
                parent,
                false
            )
        ) {

        private var img_profile: ImageView? = null
        private var tv_profileName: TextView? = null
        private var tv_address: TextView? = null
        private var recyclerView: RecyclerView? = null

        init {
            img_profile = itemView.findViewById(R.id.img_profile)
            tv_profileName = itemView.findViewById(R.id.tv_profileName)
            tv_address = itemView.findViewById(R.id.tv_address)
            recyclerView = itemView.findViewById(R.id.recycler_images)
        }

        fun bind(int: Int) {

            recyclerView!!.adapter = HomeChildRecyclerAdapter(
                listOf<Int>(
                    R.drawable.img_food_second,
                    R.drawable.img_food_first,
                    R.drawable.img_food_second,
                    R.drawable.img_food_first
                )
            )


          // GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

            recyclerView!!.layoutManager =      LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        }

    }

}