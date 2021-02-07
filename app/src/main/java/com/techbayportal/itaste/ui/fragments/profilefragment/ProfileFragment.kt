package com.techbayportal.itaste.ui.fragments.profilefragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
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
import kotlinx.android.synthetic.main.layout_changepasswordfragment.*
import kotlinx.android.synthetic.main.layout_profilefragment.*
import java.lang.Exception

@AndroidEntryPoint
class ProfileFragment : BaseFragment<LayoutProfilefragmentBinding ,ProfileViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_profilefragment
    override val viewModel: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

     var vendorProfileDetailData: VendorProfileDetailData? = null

    val postsList = ArrayList<PostDetailData>()
    private lateinit var postsRecyclerAdapter: PostsRecyclerAdapter
    var isFallowing :Boolean = false

    val loginSession = LoginSession.getInstance().getLoginResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToNetworkLiveData()
       // mViewModel.hitGetVendorProfileDetails(loginSession!!.data.id.toInt())
        mViewModel.hitGetVendorProfileDetails(177)
       // mViewModel.hitSetFollowApi(31)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializing()
        //isFallowingVendor(isFallowing)

        if (vendorProfileDetailData != null) {
            setData(vendorProfileDetailData!!)
        }


    }

    private fun initializing() {
       postsRecyclerAdapter = PostsRecyclerAdapter(postsList, object : PostsRecyclerAdapter.ClickItemListener{
            override fun onClicked(postDetailData: PostDetailData) {
                Toast.makeText(requireContext(), "Location selected: ${postDetailData.id}", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(recycler_profilePosts).navigate(R.id.action_profileFragment_to_postdetailfragment)
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
            Navigation.findNavController(tv_vendorUserName).popBackStack()
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
                    setData(it.data.data)
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
                    isFallowing = it.data!!.data.follow
                    isFallowingVendor(isFallowing)

                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }
    private fun isFallowingVendor(isFallowing :Boolean){
        if(isFallowing){
            Toast.makeText(requireContext(), "IS Following: $isFallowing", Toast.LENGTH_SHORT).show()
            btn_follow.text = getString(R.string.following)
            btn_follow.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.btn_direct_message_curve)
        }else{
            Toast.makeText(requireContext(), "IS Following: $isFallowing", Toast.LENGTH_SHORT).show()
            btn_follow.text = getString(R.string.follow)
            btn_follow.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.btn_orange_curve_profile)
        }
    }

    private fun setData(vendorProfileDetailData: VendorProfileDetailData){
        try {
            Picasso.get().load(vendorProfileDetailData.image).fit().placeholder(R.drawable.placeholder_image).centerCrop().into(sivProfileImage , object :Callback{
                override fun onSuccess() {
                    spinKit.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    spinKit.visibility = View.GONE
                }

            })
            tv_vendorUserName.text = vendorProfileDetailData.username
            tv_profileName.text = vendorProfileDetailData.first_name + vendorProfileDetailData.last_name
            tv_details.text = vendorProfileDetailData.bio
            tv_postCounts.text = vendorProfileDetailData.total_post.toString()
            tv_likesCount.text = vendorProfileDetailData.total_likes.toString()
            tv_followersCount.text = vendorProfileDetailData.total_followers.toString()
            if(!vendorProfileDetailData.posts.isNullOrEmpty()){
                postsList.addAll(vendorProfileDetailData.posts)
                postsRecyclerAdapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
        }

    }


}