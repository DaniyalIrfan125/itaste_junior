package com.techbayportal.itaste.ui.fragments.savedpostsfragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllSavedData
import com.techbayportal.itaste.databinding.ItemLayoutPostsBinding
import java.lang.Exception

class GetAllSavedPostRecyclerAdapter (private val list: ArrayList<GetAllSavedData>,
private val listener: ClickItemListener) :
RecyclerView.Adapter<GetAllSavedPostRecyclerAdapter.ViewHolder>() {

    interface ClickItemListener {
        fun onClicked(postDetailData: GetAllSavedData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLayoutPostsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if(model.image.isNotEmpty()){
            Picasso.get().load(model.image).fit().centerCrop()
                .into(holder.bind.imgProduct, object : Callback {
                    override fun onSuccess() {
                        holder.bind.skPostImage.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        Picasso.get().load(R.drawable.placeholder_image).into(holder.bind.imgProduct)
                        holder.bind.skPostImage.visibility = View.GONE
                    }

                })
        }
        else{
            // holder.bind.imgProduct.setImageDrawable(ContextCompat.getDrawable(holder.bind.imgProduct.context,R.drawable.placeholder_image))
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

