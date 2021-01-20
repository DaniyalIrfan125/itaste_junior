package com.techbayportal.itaste.ui.fragments.profilefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.GetAllCountriesData
import com.techbayportal.itaste.data.models.PostDetailData
import com.techbayportal.itaste.data.models.VendorProfileDetailData
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.LayoutProfilefragmentBinding
import com.techbayportal.itaste.ui.fragments.profilefragment.adapter.PostsRecyclerAdapter
import com.techbayportal.itaste.ui.fragments.signupconfigurationsfragment.adapter.UserLocationAdapter
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_profilefragment.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<LayoutProfilefragmentBinding ,ProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_profilefragment
    override val viewModel: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    private lateinit var mView: View
    lateinit var vendorProfileDetailData: VendorProfileDetailData

    val postsList = ArrayList<PostDetailData>()
    private lateinit var postsRecyclerAdapter: PostsRecyclerAdapter

    val loginSession = LoginSession.getInstance().getLoginResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
        mViewModel.hitGetVendorProfileDetails(loginSession!!.data.id.toInt())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        initializing()

        if (this::vendorProfileDetailData.isInitialized) {
            setData(vendorProfileDetailData)
        }



        /*recycler_profilePosts.adapter = PostsRecyclerAdapter(
            listOf<Int>(
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
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first,
                R.drawable.img_food_second,
                R.drawable.img_food_first)
        )*/

    }

    private fun initializing() {
       postsRecyclerAdapter = PostsRecyclerAdapter(postsList, object : PostsRecyclerAdapter.ClickItemListener{
            override fun onClicked(postDetailData: PostDetailData) {
                Toast.makeText(requireContext(), "Location selected: ${postDetailData.id}", Toast.LENGTH_SHORT).show()
            }
        })
        recycler_profilePosts.adapter = postsRecyclerAdapter

        recycler_profilePosts.layoutManager =  GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        recycler_profilePosts.autoFitColumns(110)

    }


    fun RecyclerView.autoFitColumns(columnWidth: Int) {
        val displayMetrics = this.context.resources.displayMetrics
        val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
        this.layoutManager = GridLayoutManager(this.context, noOfColumns)
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(mView).popBackStack()
        })

        mViewModel.onFollowButtonClicked.observe(this, Observer {
            mViewModel.hitSetFollowApi(loginSession!!.data.id.toInt())
        })
    }

    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.getVendorProfileDetailResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    vendorProfileDetailData = it.data!!.data
                    setData(vendorProfileDetailData)
                   // postsRecyclerAdapter.notifyDataSetChanged()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.getSetFollowResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "Follow", Toast.LENGTH_SHORT).show()
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    private fun setData(vendorProfileDetailData: VendorProfileDetailData){
        Picasso.get().load(vendorProfileDetailData.image).fit().centerCrop().into(sivProfileImage)
        //tv_profileName.text = vendorProfileDetailData.
        tv_details.text = vendorProfileDetailData.bio
        tv_postCounts.text = vendorProfileDetailData.total_post.toString()
        tv_likesCount.text = vendorProfileDetailData.total_likes.toString()
        tv_followersCount.text = vendorProfileDetailData.total_followers.toString()
        if(!vendorProfileDetailData.posts.isNullOrEmpty()){
            postsList.addAll(vendorProfileDetailData.posts)
            postsRecyclerAdapter.notifyDataSetChanged()
        }

    }


}