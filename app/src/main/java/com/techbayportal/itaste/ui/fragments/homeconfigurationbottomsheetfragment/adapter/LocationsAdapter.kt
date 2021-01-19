package com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment.adapter.UserLocationAdapter

class LocationsAdapter (private val list: List<GetAllCountriesData>,
    private val listener: ClickItemListener
)

    : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    interface ClickItemListener {
        fun onClicked(getAllCountriesData: GetAllCountriesData)
    }

    /*  private var onClickListener: UserLocationRvClickListener? = null

      fun setOnEntryClickListener(onEntryClickListener: UserLocationRvClickListener?) {
          onClickListener = onEntryClickListener
      }*/


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: LocationsAdapter.ViewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.item_user_location_flag,
                parent,
                false
            )
        ) {

        private var ivCountryFlag: ImageView? = null

        init {
            ivCountryFlag = itemView.findViewById(R.id.iv_country_flag)

        }

        fun bind(int: Int) {
            val model = list[int]
            var url = model.flag

            if(model.select){

            }else{
                Picasso.get()
                    .load(url).fit().centerCrop()
                    .into(ivCountryFlag)
            }

            itemView.setOnClickListener {
                listener.onClicked(model)
            }

        }


    }
}