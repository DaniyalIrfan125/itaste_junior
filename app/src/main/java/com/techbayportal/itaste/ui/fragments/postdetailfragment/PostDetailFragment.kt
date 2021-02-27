package com.techbayportal.itaste.ui.fragments.postdetailfragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.data.models.*
import com.techbayportal.itaste.data.remote.Resource
import com.techbayportal.itaste.databinding.FragmentPostDetailBinding
import com.techbayportal.itaste.ui.fragments.postdetailbottomsheetfragment.PostDetailBottomSheetFragment
import com.techbayportal.itaste.ui.fragments.postdetailfragment.adapter.PostCommentsAdapter
import com.techbayportal.itaste.ui.fragments.postdetailfragment.itemclicklistener.PostCommentsRvClickListener
import com.techbayportal.itaste.utils.DialogClass
import com.techbayportal.itaste.utils.LoginSession
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_post_detail.img_back
import kotlinx.android.synthetic.main.layout_postfragment.*
import kotlinx.android.synthetic.main.layout_profile_chat_comment.*
import kotlinx.android.synthetic.main.layout_write_message.tvPostMessage
import www.sanju.motiontoast.MotionToast
import java.lang.Exception


@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding, PostDetailFragmentViewModel>(),
    PostCommentsRvClickListener {


    override val layoutId: Int
        get() = R.layout.fragment_post_detail
    override val viewModel: Class<PostDetailFragmentViewModel>
        get() = PostDetailFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    var commentsList: ArrayList<Comment> = ArrayList()
    lateinit var commentsAdapter: PostCommentsAdapter
    val bottomSheet = PostDetailBottomSheetFragment()

    var postDetailResponse: PostDetailResponse? = null
    private var vendorId: Int = 0

    val loginSession = LoginSession.getInstance().getLoginResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.postDetailCall(sharedViewModel.postId)
        mViewModel.getCategories()

        subscribeToNetworkLiveData()
    }


    override fun subscribeToNetworkLiveData() {
        super.subscribeToNetworkLiveData()

        mViewModel.postDeleteResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        DialogClass.successDialog(requireContext(), getString(R.string.tv_post_delete), baseDarkMode)
                        /*MotionToast.createToast(
                            requireActivity(),
                            getString(R.string.tv_success),
                            getString(R.string.tv_post_delete),
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(requireActivity(), R.font.roboto_regular)
                        )*/

                        Navigation.findNavController(radioGroup)
                            .popBackStack(R.id.homeFragment, false)
                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })



        mViewModel.getCategoriesResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            sharedViewModel.categoriesResponse.value = it.data
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.postDetailResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            postDetailResponse = it
                            populateDate(postDetailResponse!!)
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.postCommentResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        //added post id
                        mViewModel.postDetailCall(sharedViewModel.postId)

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.favoriteUnfavoriteReponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                  //  loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                      //  loadingDialog.dismiss()


                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.favoriteUnfavoriteCommentReponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                   // loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                       // loadingDialog.dismiss()


                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })



        mViewModel.allowCommentsResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {
                            mViewModel.postDetailCall(sharedViewModel.postId)
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })


        mViewModel.addToCartResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()
                        it.data?.let {

                            //show success dialog
                            DialogClass.successDialog(requireContext(), getString(R.string.post_added_to_cart), baseDarkMode)

                            /*MotionToast.createToast(
                                requireActivity(),
                                "Post added to cart",
                                "Post added to cart",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(requireActivity(), R.font.roboto_regular)
                            )*/
                        }

                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })

        mViewModel.savePostResponse.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    loadingDialog.show()
                }
                Resource.Status.SUCCESS -> {
                    it?.let { it ->
                        loadingDialog.dismiss()

                        if (it.data!!.data.is_save) {
                            img_SavePost.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.icon_savedpost
                                )
                            )
                        } else {
                            img_SavePost.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.img_savepost
                                )
                            )
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    loadingDialog.dismiss()
                    DialogClass.errorDialog(requireContext(), it.message!!, baseDarkMode)
                }
            }
        })
    }

    private fun populateDate(it: PostDetailResponse) {
        Picasso.get().load(it.data.post.image).fit().centerCrop().into(imageProduct, object :
            Callback {
            override fun onSuccess() {
                spinKit_post1.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                spinKit_post1.visibility = View.GONE
            }
        })

        Picasso.get().load(it.data.vendor.profilePic).fit().centerCrop().into(img_profile, object :
            Callback {
            override fun onSuccess() {
                if(spinKit_profile != null){
                    spinKit_profile.visibility = View.GONE
                }
            }

            override fun onError(e: Exception?) {
                if(spinKit_profile != null){
                    spinKit_profile.visibility = View.GONE
                }

            }
        })

        commentsList.clear()
        if (it.data.post.comments != null)
            commentsList.addAll(it.data.post.comments)
        commentsAdapter.notifyDataSetChanged()

        if (commentsList.size > 6) {
            tv_viewAllComments.visibility = View.VISIBLE
            tv_viewAllComments.text =
                getString(R.string.tv_viewAll) + " " + commentsList.size + " " + getString(R.string.tv_comments)
        } else {
            tv_viewAllComments.visibility = View.GONE
        }

        vendorId = it.data.vendor.vendor_id
        tv_vendorName.text = it.data.vendor.first + " " + it.data.vendor.last
        tv_vendorLocation.text = it.data.vendor.location
        tv_title.text = it.data.post.caption
        tv_price.text = it.data.post.price.toString() + " " + getString(R.string.aed)
        tv_description.text = it.data.post.description
        if(it.data.post.description.isNullOrEmpty()){
            tv_description.visibility =View.GONE
        }else{
            tv_description.visibility =View.VISIBLE
        }
        tv_totalLikes.text =
            it.data.post.total_likes.toString() + " " + getString(R.string.tv_likes)
        tv_time.text = getString(R.string.tv_cooking_time_is) + " : " + it.data.post.cooking_time
        mLikeButton.isLiked = it.data.post.is_favourite

        sharedViewModel.isCommentsAreOnOrOff.value = it.data.post.allow_comments
    }

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onSaveButtonClicked.observe(this, Observer {
            mViewModel.savePost(sharedViewModel.postId)


        })

        mViewModel.onAddButtonClicked.observe(this, Observer {
            mViewModel.addToCart(sharedViewModel.postId)
        })

        mViewModel.onVendorProfileHeaderClicked.observe(this, Observer {
          //  sharedViewModel.vendorProfileId = vendorId
            sharedViewModel.vendorHomeScreenData = GetHomeScreenData(vendorId,"","","","",arrayListOf<GetHomeScreenPostsData>())
            Navigation.findNavController(ll_dp).navigate(R.id.action_postDetailFragment_to_profileFragment)
        })

        mViewModel.onSendButtonClicked.observe(this, Observer {
            sharedViewModel.vendorDetailsForCart =
                CartVendor(sharedViewModel.vendorHomeScreenData!!.location, sharedViewModel.vendorHomeScreenData!!.first, sharedViewModel.vendorHomeScreenData!!.id, sharedViewModel.vendorHomeScreenData!!.first, sharedViewModel.vendorHomeScreenData!!.profilePic)
           // sharedViewModel.cartPost = null
            Navigation.findNavController(iv_Chat)
                .navigate(R.id.action_postDetailFragment_to_chatFragment2)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onEditPostButtonClicked.observe(this, Observer {
            sharedViewModel.isEditPost = true
            Navigation.findNavController(btn_edit_post).navigate(R.id.action_postDetailFragment_to_postFragment)
        })

        mViewModel.onMoreButtonClicked.observe(this, Observer {
            bottomSheet.show(requireActivity().supportFragmentManager, "postDetailBottomSheet")
        })


        ivComment.setOnClickListener {
            layoutComment.visibility = View.VISIBLE
            view?.let { it1 -> openSoftKeyboard(requireContext(), it1) }
        }


        mLikeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                mViewModel.favouriteUnFavouriteCall(sharedViewModel.postId)
            }

            override fun unLiked(likeButton: LikeButton?) {
                mViewModel.favouriteUnFavouriteCall(sharedViewModel.postId)
            }
        })

        tvPostMessage.setOnClickListener {
            if (!TextUtils.isEmpty(ed_comment.text)) {

                mViewModel.postCommentCall(sharedViewModel.postId, ed_comment.text.toString())
                ed_comment.setText("")
            } else {
                Snackbar.make(
                    ed_comment,
                    getString(R.string.tv_plz_write_comment),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


        tv_viewAllComments.setOnClickListener {
            commentsAdapter.updateShowAllItems(true)
            commentsAdapter.notifyDataSetChanged()
            tv_viewAllComments.visibility = View.GONE
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialising()
        //Check if current login vendor and selected post vendor
        if(loginSession != null){
            if(sharedViewModel.vendorProfileId  == loginSession.data.id){
                rippleEdtPost.visibility = View.VISIBLE
                rippleAddToCart.visibility = View.GONE
            }else{
                rippleEdtPost.visibility = View.GONE
                rippleAddToCart.visibility = View.VISIBLE
            }
        }

        postDetailResponse?.let {
            populateDate(it)
        }

    }


    override fun subscribeToShareLiveData() {
        super.subscribeToShareLiveData()

        sharedViewModel.isEditBottomSheetClicked.observe(this, Observer {

            if (it) {
                sharedViewModel.isEditPost = true
                Navigation.findNavController(btn_edit_post)
                    .navigate(R.id.action_postDetailFragment_to_postFragment)
            }
        })

        sharedViewModel.isPostUpdated.observe(this, Observer {
            if (it) {

                mViewModel.postDetailCall(sharedViewModel.postId)
                sharedViewModel.isPostUpdated.value = false
            }
        })

        sharedViewModel.isPostDetailDeleteClicked.observe(this, Observer {
            if (it) {
                mViewModel.deletePost(sharedViewModel.postId)
                sharedViewModel.isPostDetailDeleteClicked.value = false
            }

        })

        sharedViewModel.isCommentOffClicked.observe(this, Observer {
            it?.let {
                if (it) {
                    mViewModel.allowComments(sharedViewModel.postId, 1)
                    sharedViewModel.isCommentOffClicked.value = null
                } else {
                    mViewModel.allowComments(sharedViewModel.postId, 0)
                    sharedViewModel.isCommentOffClicked.value = null
                }
            }
        })

    }

    private fun initialising() {
        commentsAdapter = PostCommentsAdapter(this, commentsList, false)
        mViewDataBinding.rvPostComments.adapter = commentsAdapter

        if (LoginSession.getInstance().getLoginResponse() != null) {
            Picasso.get().load(LoginSession.getInstance().getLoginResponse()!!.data.profile_pic)
                .fit().centerCrop().into(user_Image, object :
                    Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {
                        user_Image.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.placeholder_image
                            )
                        )
                    }
                })

        }
    }

    override fun onItemClickListener() {

    }

    override fun favouriteUnFavorite(commentId: Int) {
        mViewModel.favouriteCommentCall(commentId)
    }

}