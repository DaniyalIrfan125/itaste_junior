package com.techbayportal.itaste.ui.fragments.homeconfigurationbottomsheetfragment.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllCountriesData


class LocationsAdapter (private val list: List<GetAllCountriesData>,
    private val listener: ClickItemListener
)

    : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {




    interface ClickItemListener {
        fun onClicked(getAllCountriesData: GetAllCountriesData){


        }
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

            Picasso.get()
                .load(url).fit().centerCrop()
                .into(ivCountryFlag)

            if(model.select){
                ivCountryFlag?.clearColorFilter()

            }else{
                val matrix = ColorMatrix()
                matrix.setSaturation(0f)
                ivCountryFlag?.colorFilter = ColorMatrixColorFilter(matrix)

            }

            itemView.setOnClickListener {
                listener.onClicked(model)
            }
        }
    }
}