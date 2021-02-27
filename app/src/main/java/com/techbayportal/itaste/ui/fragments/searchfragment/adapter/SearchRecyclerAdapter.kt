package com.techbayportal.itaste.ui.fragments.searchfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllBlockedUserData
import com.techbayportal.itaste.data.models.SearchAndFilterResponseData
import com.techbayportal.itaste.databinding.ItemSearchDoubleImageBinding
import com.techbayportal.itaste.databinding.ItemSearchThreeImageBinding
import com.techbayportal.itaste.ui.fragments.blockedaccountsfragment.adapter.BlockedAccountsAdapter
import kotlinx.android.synthetic.main.item_search_double_image.view.*
import kotlinx.android.synthetic.main.item_search_three_image.view.*
import kotlinx.android.synthetic.main.layout_searchfragment.view.*
import timber.log.Timber
import java.lang.Exception


class SearchRecyclerAdapter(
    private val list: ArrayList<ArrayList<SearchAndFilterResponseData>>,
    val context: Context,
    private val listener: ClickItemListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ClickItemListener {
        fun onClicked(searchAndFilterResponseData: SearchAndFilterResponseData)
    }

    inner class ViewHolderClassDouble(val itemBinding: ItemSearchDoubleImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val bind = itemBinding

        var imgFirst: ImageView = itemBinding.imgFirst
        var imgSecond: ImageView = itemBinding.imgSecond

    }

    inner class ViewHolderClassTripple(val itemBinding: ItemSearchThreeImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        var imgThird: ImageView = itemBinding.imgThird
        var imgForth: ImageView = itemBinding.imgFourth
        var imgFifth: ImageView = itemBinding.imgFifth
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            ViewHolderClassDouble(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_double_image,
                    parent,
                    false
                )
            )
        } else {
            ViewHolderClassTripple(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_search_three_image,
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        //holder.itemView.img_first = model.image
        /*
        * Line 1 item 1
        * */
        if (model.size == 2) {
            if (model[0].image.isNotEmpty()) {
                Picasso.get().load(model[0].image).fit().centerCrop()
                    .into(holder.itemView.img_first, object : Callback {
                        override fun onSuccess() {
//                            holder.itemView.spinKit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image)
                                .into(holder.itemView.img_first)
                            // holder.itemView.spinKit.visibility = View.GONE
                        }

                    })

                holder.itemView.img_first.setOnClickListener(View.OnClickListener {
                    // Toast.makeText(context, "Post Clicked : ${model[0].id}", Toast.LENGTH_SHORT).show()
                    Timber.d("Post Clicked : ${model[0].id}")
                    listener.onClicked(model[0])
                })
            }

            /*
        * Line 1 item 2
        * */
            if (model[1].image.isNotEmpty()) {
                Picasso.get().load(model[1].image).fit().centerCrop()
                    .into(holder.itemView.img_second, object : Callback {
                        override fun onSuccess() {
                            //    holder.itemView.spinKit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image)
                                .into(holder.itemView.img_second)
                            //    holder.itemView.spinKit.visibility = View.GONE
                        }

                    })
                holder.itemView.img_second.setOnClickListener(View.OnClickListener {
                    // Toast.makeText(context, "Post Clicked : ${model[1].id}", Toast.LENGTH_SHORT).show()
                    Timber.d("Post Clicked : ${model[1].id}")
                    listener.onClicked(model[1])
                })
            }
        } else {

/*
        * Line 2 item 1
        * */
            if (model[0].image.isNotEmpty()) {
                Picasso.get().load(model[0].image).fit().centerCrop()
                    .into(holder.itemView.img_third, object : Callback {
                        override fun onSuccess() {
                            //  holder.itemView.spinKit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image)
                                .into(holder.itemView.img_third)
                            //  holder.itemView.spinKit.visibility = View.GONE
                        }

                    })

                holder.itemView.img_third.setOnClickListener(View.OnClickListener {
                    //Toast.makeText(context, "Post Clicked : ${model[0].id}", Toast.LENGTH_SHORT).show()
                    Timber.d("Post Clicked : ${model[0].id}")
                    listener.onClicked(model[0])
                })
            }
/*
        * Line 2 item 2
        * */
            if (model[1].image.isNotEmpty()) {
                Picasso.get().load(model[1].image).fit().centerCrop()
                    .into(holder.itemView.img_fourth, object : Callback {
                        override fun onSuccess() {
                            //   holder.itemView.spinKit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image)
                                .into(holder.itemView.img_fourth)
                            //      holder.itemView.spinKit.visibility = View.GONE
                        }

                    })
                holder.itemView.img_fourth.setOnClickListener(View.OnClickListener {
                    // Toast.makeText(context, "Post Clicked : ${model[1].id}", Toast.LENGTH_SHORT).show()
                    Timber.d("Post Clicked : ${model[1].id}")
                    listener.onClicked(model[1])
                })

            }
            /*
        * Line 2 item 3
        * */
            if (model[2].image.isNotEmpty()) {
                Picasso.get().load(model[2].image).fit().centerCrop()
                    .into(holder.itemView.img_fifth, object : Callback {
                        override fun onSuccess() {
                            //        holder.itemView.spinKit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image)
                                .into(holder.itemView.img_fifth)
                            //     holder.itemView.spinKit.visibility = View.GONE
                        }

                    })

                holder.itemView.img_fifth.setOnClickListener(View.OnClickListener {
                    //Toast.makeText(context, "Post Clicked : ${model[2].id}", Toast.LENGTH_SHORT).show()
                    Timber.d("Post Clicked : ${model[2].id}")
                    listener.onClicked(model[2])
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0)
            1
        else 2
    }

}