package com.techbayportal.itaste.ui.fragments.homefragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.databinding.ItemHomeRecyclerImageBinding
import com.techbayportal.itaste.ui.fragments.homefragment.HomeFragment
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeChildRvClickListener
import com.techbayportal.itaste.ui.fragments.homefragment.itemclicklistener.HomeRvClickListener

class HomeChildRecyclerAdapter(private val list: List<Int>) :
    RecyclerView.Adapter<HomeChildRecyclerAdapter.ViewHolder>() {

    private var onClickListener: HomeChildRvClickListener? = null

    fun setOnChildClickListener(onEntryClickListener: HomeChildRvClickListener?) {
        onClickListener = onEntryClickListener
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
                R.layout.item_home_recycler_image,
                parent,
                false
            )
        ) {

        private var imageViewProduct: ImageView? = null

        init {
            imageViewProduct = itemView.findViewById(R.id.img_product)
        }

        fun bind(position: Int) {
            val imageProduct: Int = list[position]
            imageViewProduct?.setOnClickListener(View.OnClickListener {
                onClickListener?.onChildItemClick(position)
            })

            Picasso.get()
                .load(imageProduct)
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