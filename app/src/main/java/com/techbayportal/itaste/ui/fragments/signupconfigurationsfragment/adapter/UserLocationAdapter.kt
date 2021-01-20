package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllCountriesData
import timber.log.Timber
import java.lang.Exception

class UserLocationAdapter(
    private val context: Context,
    private val list: List<GetAllCountriesData>,
    private val listener: ClickItemListener):
    RecyclerView.Adapter<UserLocationAdapter.ViewHolder>() {

    interface ClickItemListener {
        fun onClicked(getAllCountriesData: GetAllCountriesData)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserLocationAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserLocationAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_user_locations,
                parent,
                false
            )
        ) {

        private var tvCountryName: TextView? = null
        private var ivCountryFlag: ImageView? = null

        init {
            tvCountryName = itemView.findViewById(R.id.tv_country_name)
            ivCountryFlag = itemView.findViewById(R.id.iv_country_flag)

        }

        fun bind(int: Int) {
            val model = list[int]
            var url = model.flag

            tvCountryName!!.text = model.name

            Picasso.get()
                .load(url).fit().centerCrop()
                .into(ivCountryFlag)

            itemView.setOnClickListener {
                listener.onClicked(model)
            }


            /*if (model.flag != null && model.flag.isNotEmpty()) {
                Picasso.get().load(model.flag).fit().centerCrop()
                    .into(ivCountryFlag, object :
                        Callback {
                        override fun onSuccess() {
                            Timber.d("on success")
                            //bind.spinkit.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                           // holder.bind.spinkit.visibility = View.GONE
                        }
                    })
            }*/

        }


    }
}