package com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllCountriesData
import kotlinx.android.synthetic.main.layout_homefragment.*
import timber.log.Timber
import java.lang.Exception

class UserLocationAdapter(
    private val context: Context,
    private val list: List<GetAllCountriesData>,
    private val listener: ClickItemListener):
    RecyclerView.Adapter<UserLocationAdapter.ViewHolder>() {

    var rowIndex = -1

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
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_user_locations, parent, false)) {

        private var tvCountryName: TextView? = null
        private var ivCountryFlag: ImageView? = null
        private var skLocation: SpinKitView? = null

        init {
            tvCountryName = itemView.findViewById(R.id.tv_country_name)
            ivCountryFlag = itemView.findViewById(R.id.iv_country_flag)
            skLocation = itemView.findViewById(R.id.sk_locations)

        }

        fun bind(position: Int) {
            val model = list[position]
            var url = model.flag

            tvCountryName!!.text = model.name
            tvCountryName!!.text = list[position].name


            if(url.isNotEmpty()){
                Picasso.get()
                    .load(url).fit().centerCrop()
                    .into(ivCountryFlag, object :Callback{
                        override fun onSuccess() {
                            Timber.d("on success User Location Image")
                            skLocation!!.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Picasso.get().load(R.drawable.placeholder_image).into(ivCountryFlag)
                            skLocation!!.visibility = View.GONE
                            Timber.d("on error User Location Image: $e")
                        }

                    })
            }


            itemView.setOnClickListener {
                listener.onClicked(model)
                rowIndex = position
                notifyDataSetChanged()
            }

            if (rowIndex == position) {

                 tvCountryName!!.setTextColor(ContextCompat.getColor(context, R.color.titleTextColorBlack))

            } else {
                tvCountryName!!.setTextColor(ContextCompat.getColor(context, R.color.location_text))

            }

        }


    }
}