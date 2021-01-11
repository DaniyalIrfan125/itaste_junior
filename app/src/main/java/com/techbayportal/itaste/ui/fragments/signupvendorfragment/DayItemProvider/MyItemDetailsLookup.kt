package com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.data.models.DaysOfWeek
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<DaysOfWeek>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<DaysOfWeek>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as DaysRecyclerAdapter.ListViewHolder)
                .getItemDetails()
        }
        return null
    }
}