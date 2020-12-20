package com.techbayportal.itaste.ui.fragments.searchfragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutSearchfragmentBinding
import com.techbayportal.itaste.ui.fragments.searchfragment.adapter.SearchRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_searchfragment.*


@AndroidEntryPoint
class SearchFragment : BaseFragment<LayoutSearchfragmentBinding, SearchViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_searchfragment
    override val viewModel: Class<SearchViewModel>
        get() = SearchViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var isLayoutOpened = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRevealLayout.setLockDrag(true)
        img_filter.setOnClickListener {

            isLayoutOpened = !swipeRevealLayout.isClosed
            isLayoutOpened = if (!isLayoutOpened) {
                swipeRevealLayout.open(true)
                true
            } else {
                swipeRevealLayout.close(true)
                false
            }
        }


        recycler_searchposts.adapter = SearchRecyclerAdapter(
            listOf<Int>(
                R.drawable.img_big_first,
                R.drawable.img_small_first,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second
            ),
            context!!
        )

        recycler_searchposts.layoutManager = LinearLayoutManager(context)


    }


}