package com.techbayportal.itaste.ui.fragments.signupvendorfragment.DayItemProvider

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemKeyProvider
import com.techbayportal.itaste.data.models.DaysOfWeek
import com.techbayportal.itaste.ui.fragments.signupvendorfragment.adapter.DaysRecyclerAdapter

class MyItemKeyProvider(private val adapter: DaysRecyclerAdapter) :
    ItemKeyProvider<DaysOfWeek>(SCOPE_CACHED) {
    override fun getKey(position: Int): DaysOfWeek? {
        return adapter.getItem(position)
    }

    override fun getPosition(key: DaysOfWeek): Int {
        return adapter.getPosition(key.dayName)
    }
    //override fun inSelectionHotspot(e: MotionEvent): Boolean { return true }
}