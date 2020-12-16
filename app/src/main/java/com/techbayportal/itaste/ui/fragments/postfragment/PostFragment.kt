package com.techbayportal.itaste.ui.fragments.postfragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutPostfragmentBinding
import com.techbayportal.itaste.ui.fragments.postfragment.adapter.LocationAdapter
import com.techbayportal.itaste.ui.fragments.postfragment.adapter.TimeDurationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_postfragment.*

@AndroidEntryPoint
class PostFragment : BaseFragment<LayoutPostfragmentBinding, PostViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_postfragment
    override val viewModel: Class<PostViewModel>
        get() = PostViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recycler_time.adapter = TimeDurationAdapter(
            listOf<String>(
                "Sunday", "1 hour 30 minutes", "3 days", " 4 minutes",
                "Sunday", "1 hour 30 minutes", "3 days", " 4 minutes"
            ),
            requireContext()
        )


        recycler_location.adapter = LocationAdapter(
            listOf<String>(
                "Dubai, UAE", "Business bay, dubai", "Al Barsha Heights", "Sharjah",
                "Diera, Naif"
            ),
            requireContext()
        )

    }
}