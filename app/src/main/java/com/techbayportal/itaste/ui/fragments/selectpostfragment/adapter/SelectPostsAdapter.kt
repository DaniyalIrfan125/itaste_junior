package com.techbayportal.itaste.ui.fragments.selectpostfragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.ybq.android.spinkit.SpinKitView
import com.techbayportal.itaste.R


class SelectPostsAdapter(private val context: Context, private val list: List<Uri>) :
    RecyclerView.Adapter<SelectPostsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onEntryClick(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageProduct: Uri = list[position]
        holder.bind(imageProduct,position)
    }

    override fun getItemCount(): Int = list.size


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_select_posts,
                parent,
                false
            )
        ) {

        private var imageViewProduct: ImageView? = null
        private var spinkit: SpinKitView? = null

        init {
            imageViewProduct = itemView.findViewById(R.id.img_productSelect)
            spinkit = itemView.findViewById(R.id.spinkit)
        }

        fun bind(file: Uri, position: Int) {

            imageViewProduct?.let {
                Glide.with(context).load(file).placeholder(R.drawable.img_placeholder)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            spinkit?.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            spinkit?.visibility = View.GONE
                            return false
                        }

                    }).into(it)
            }

            imageViewProduct?.setOnClickListener {
                onClickListener?.let {
                    it.onEntryClick(position)
                }
            }

        }

    }
}

