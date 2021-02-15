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
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutSavedpostsBinding
import com.techbayportal.itaste.ui.fragments.savedpostsfragment.adapter.GetAllSavesPostRecyclerAdapterMultiSelection
import com.techbayportal.itaste.utils.DialogClass
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_savedposts.*
import kotlinx.android.synthetic.main.layout_savedposts.img_back
import kotlinx.android.synthetic.main.layout_savedposts.recycler_posts
import kotlinx.android.synthetic.main.layout_selectpost.*
import timber.log.Timber

@AndroidEntryPoint
class SavedPostsFragment : BaseFragment<LayoutSavedpostsBinding, SavedPostsViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_savedposts
    override val viewModel: Class<SavedPostsViewModel>
        get() = SavedPostsViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


    lateinit var mView: View
    private var selectedPostItems: ArrayList<Int> = ArrayList()
    lateinit var postsRecyclerAdapter: GetAllSavesPostRecyclerAdapterMultiSelection
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

                        getAllSavedData.clear()
                        getAllSavedData.addAll(it.data!!.data)

                        postsRecyclerAdapter.clearSelection()
                        ripple_remove.visibility = View.GONE
                        postsRecyclerAdapter.notifyDataSetChanged()

                        if(getAllSavedData.size == 0 ){
                            ll_noPosts.visibility = View.VISIBLE
                        }
                        else{
                            ll_noPosts.visibility = View.GONE
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })



        mViewModel.removeSavedPostResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        mViewModel.allSavedPost()

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

        mViewModel.onRemoveButtonClicked.observe(this, Observer {
            if (postsRecyclerAdapter.getSelectedItems().isNotEmpty()) {
                selectedPostItems.clear()

                for (postIds in postsRecyclerAdapter.getSelectedItems()) {
                    selectedPostItems.add(getAllSavedData[postIds].id)
                }

                mViewModel.removeSavedPost(selectedPostItems)
            }
        })
        mViewModel.onCancelButtonClicked.observe(this, Observer {
            postsRecyclerAdapter.clearSelection()
            ripple_remove.visibility = View.GONE

        })


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        postsRecyclerAdapter = GetAllSavesPostRecyclerAdapterMultiSelection(
            requireContext(), getAllSavedData
        )


        recycler_posts.adapter = postsRecyclerAdapter
        recycler_posts.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recycler_posts.autoFitColumns(110)

        postsRecyclerAdapter.setItemClick(object :
            GetAllSavesPostRecyclerAdapterMultiSelection.OnItemClick {
            override fun onItemClick(view: View?, inbox: GetAllSavedData?, position: Int) {
                if (postsRecyclerAdapter.selectedItemCount() > 0) {
                    toggleSelection(position)
                } else{
                    Timber.d("SavePost item clicked : $position")
                    sharedViewModel.postId = position
                    Navigation.findNavController(mView).navigate(R.id.action_savedPostsFragment_to_postDetailFragment)


                }
            }

            override fun onLongPress(view: View?, inbox: GetAllSavedData?, position: Int) {
                toggleSelection(position)
            }
        })


    }

    /*
       toggle selection of items and show the count of selected items on the action bar
     */
    private fun toggleSelection(position: Int) {
        postsRecyclerAdapter.toggleSelection(position)
        val count: Int = postsRecyclerAdapter.selectedItemCount()
        if (count == 0) {
            ripple_remove.visibility = View.GONE
        } else {
            ripple_remove.visibility = View.VISIBLE
        }
    }


    private fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }


}