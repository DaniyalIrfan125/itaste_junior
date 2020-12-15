package com.techbayportal.itaste.ui.fragments.searchfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignSelf
import com.google.android.flexbox.FlexboxLayoutManager
import com.techbayportal.itaste.R


class SearchRecyclerAdapter(private val list: List<Int>, val context: Context) :
    RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageProduct: Int = list[position]
        holder.bind(imageProduct)
    }

    override fun getItemCount(): Int = list.size


  inner  class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_search_image,
                parent,
                false
            )
        ) {

        private var imageViewProduct: ImageView? = null

        init {
            imageViewProduct = itemView.findViewById(R.id.image_search)

        }

        fun bind(int: Int) {


            Glide.with(context).asDrawable().load(int).into(imageViewProduct!!)
            val lp = imageViewProduct!!.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams) {
                lp.flexGrow = 1f
            }
//            Picasso.get()
//                .load(int)
//                .fit()
//                .centerCrop()
//                .into(imageViewProduct, object : Callback {
//                    override fun onSuccess() {
//
//                    }
//
//                    override fun onError(e: Exception) {
//
//                    }
//                })

        }

    }
}

