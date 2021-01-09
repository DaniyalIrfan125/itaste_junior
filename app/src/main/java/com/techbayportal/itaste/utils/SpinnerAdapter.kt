package com.techbayportal.itaste.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.techbayportal.itaste.R
import com.techbayportal.itaste.data.models.GetAllCitiesData

import com.techbayportal.itaste.data.models.GetAllCountriesData


class SpinnerAdapter<T : Any?>
constructor(
    private val listOfObjects: ArrayList<T>
) : BaseAdapter() {
    override fun getCount(): Int {
        return listOfObjects.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutView =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_spinner, parent, false)
        val textView = layoutView.findViewById<TextView>(R.id.txtSpinner)
        if (listOfObjects[position] is GetAllCountriesData)
            textView.text = (listOfObjects[position] as GetAllCountriesData).name
        if (listOfObjects[position] is GetAllCitiesData)
            textView.text = (listOfObjects[position] as GetAllCitiesData).name
        return layoutView
    }
}