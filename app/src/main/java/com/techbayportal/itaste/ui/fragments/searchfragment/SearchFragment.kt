package com.techbayportal.itaste.ui.fragments.searchfragment

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.GetAllCategoriesData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSearchfragmentBinding
import com.techbayportal.itaste.ui.fragments.searchfragment.adapter.SearchRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
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
    private val categoryList = ArrayList<GetAllCategoriesData>()

    var categoryId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
        //mViewModel.hitGetAllBlockedAccountsApi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // initializing()
        swipeRevealLayout.setLockDrag(true)
        img_filter.setOnClickListener {

            isLayoutOpened = !swipeRevealLayout.isClosed
            isLayoutOpened = if (!isLayoutOpened) {
                swipeRevealLayout.open(true)
                mViewModel.hitGetAllBlockedAccountsApi()
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
            requireContext()
        )

        recycler_searchposts.layoutManager = LinearLayoutManager(context)


    }

    private fun initializing() {
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getAllCategoriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    categoryList.clear()
                    categoryList.addAll(it.data!!.data)
                    createRadioButton(categoryList)
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }

            }
        })

    }

    private fun createRadioButton(data: List<GetAllCategoriesData>) {
        val rb = arrayOfNulls<RadioButton>(data.size)
        for (i in data.indices) {
            rb[i] = RadioButton(requireContext())
            rb[i]!!.text = data[i].name
            rb[i]!!.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.titleTextColorBlack
                )
            )
            rb[i]!!.textSize = 14f
            val font = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
            rb[i]!!.typeface = font
            rb[i]!!.id = data[i].id
            rg_categories.addView(rb[i])
        }

    }


}