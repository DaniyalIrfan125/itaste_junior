package com.techbayportal.itaste.ui.fragments.cartfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayoutManager
import com.techbayportal.itaste.R
import com.techbayportal.itaste.ui.fragments.searchfragment.adapter.SearchRecyclerAdapter

class CartsAdapter (private val list: List<Int>, val context: Context) :
    RecyclerView.Adapter<CartsAdapter.ViewHolder>() {

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
                R.layout.item_layout_cart,
                parent,
                false
            )
        ) {

        private var img_product: ImageView? = null

        init {
            img_product = itemView.findViewById(R.id.img_product)

        }

        fun bind(int: Int) {


            Glide.with(context).asDrawable().load(int).into(img_product!!)

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

