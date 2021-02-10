package com.techbayportal.itaste.ui.fragments.savedpostsfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.GetAllSavedData
import com.techbayportal.itaste.data.models.GetAllSavedPost
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSavedpostsBinding
import com.techbayportal.itaste.ui.fragments.profilefragment.adapter.PostsRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.savedpostsfragment.adapter.GetAllSavedPostRecyclerAdapter
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_savedposts.*

@AndroidEntryPoint
class SavedPostsFragment : BaseFragment<LayoutSavedpostsBinding, SavedPostsViewModel>(),
    GetAllSavedPostRecyclerAdapter.ClickItemListener {
    override val layoutId: Int
        get() = R.layout.layout_savedposts
    override val viewModel: Class<SavedPostsViewModel>
        get() = SavedPostsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    lateinit var postsRecyclerAdapter: GetAllSavedPostRecyclerAdapter
    var getAllSavedData: ArrayList<GetAllSavedData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.allSavedPost()

        subscribeToNetworkLiveData()
    }


    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getSavedPostResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        getAllSavedData.addAll(it.data!!.data)
                        postsRecyclerAdapter.notifyDataSetChanged()

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        postsRecyclerAdapter = GetAllSavedPostRecyclerAdapter(
            getAllSavedData, this)


        recycler_posts.adapter = postsRecyclerAdapter
        recycler_posts.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recycler_posts.autoFitColumns(110)
    }


    private fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns =
            ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }

    override fun onClicked(postDetailData: GetAllSavedData) {
        TODO("Not yet implemented")
    }

}