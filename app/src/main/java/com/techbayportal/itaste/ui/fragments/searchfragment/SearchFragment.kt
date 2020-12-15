package com.techbayportal.itaste.ui.fragments.searchfragment

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.*
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_filter.setOnClickListener(View.OnClickListener {

        })


        val flexboxLayoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }
        recycler_searchposts.layoutManager = flexboxLayoutManager

        recycler_searchposts.adapter = SearchRecyclerAdapter(
            listOf<Int>(
                R.drawable.img_food_first,
                R.drawable.img_food_second,
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




//        val spannedGridLayoutManager = SpannedGridLayoutManager(orientation = SpannedGridLayoutManager.Orientation.VERTICAL, spans = 3)
//        spannedGridLayoutManager.itemOrderIsStable = true
//
//
//
//
//        spannedGridLayoutManager.spanSizeLookup = SpannedGridLayoutManager.SpanSizeLookup { position ->
//            when {
//                position == 0 -> {
//                    /**
//                     * 150f is now static
//                     * should calculate programmatically in runtime
//                     * for to manage header hight for different resolution devices
//                     */
//                    SpanSize(3, 1)
//                }
//                position % 7 == 1 ->
//                    SpanSize(2, 2)
//                else ->
//                    SpanSize(1, 1)
//            }
//        }




    }



}