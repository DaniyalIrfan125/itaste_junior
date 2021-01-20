package com.techbayportal.itaste.ui.fragments.profilefragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.PostDetailData
import com.techbayportal.itaste.data.models.VendorProfileDetailData
import com.techbayportal.itaste.databinding.ItemLayoutPostsBinding

class PostsRecyclerAdapter(
    private val list: ArrayList<PostDetailData>,
    private val listener: ClickItemListener) :
    RecyclerView.Adapter<PostsRecyclerAdapter.ViewHolder>() {

    interface ClickItemListener {
        fun onClicked(postDetailData: PostDetailData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutPostsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if(model.image.isNotEmpty()){
            Picasso.get().load(model.image).fit().centerCrop()
                .into(holder.bind.imgProduct)
        }
        else{
            holder.bind.imgProduct.setImageDrawable(ContextCompat.getDrawable(holder.bind.imgProduct.context,R.drawable.placeholder_image))
        }
        holder.itemView.setOnClickListener {
              listener.onClicked(model)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private var binding: ItemLayoutPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val bind = binding
    }
}

