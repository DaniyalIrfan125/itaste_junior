package com.techbayportal.itaste.ui.fragments.profilefragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.ui.fragments.homefragment.adapter.HomeChildRecyclerAdapter

class PostsRecyclerAdapter(private val list: List<Int>) :
    RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageProduct: Int = list[position]
        holder.bind(imageProduct)
    }

    override fun getItemCount(): Int = list.size


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_layout_posts,
                parent,
                false
            )
        ) {

        private var imageViewProduct: ImageView? = null

        init {
            imageViewProduct = itemView.findViewById(R.id.img_product)
        }

        fun bind(int: Int) {

            Picasso.get()
                .load(int)
                .fit()
                .centerCrop()
                .into(imageViewProduct, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception) {

                    }
                })

        }

    }
}

