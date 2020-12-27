package com.techbayportal.itaste.ui.fragments.postdetailfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentPostDetailBinding
import com.techbayportal.itaste.ui.fragments.postdetailfragment.adapter.PostCommentsAdapter
import com.techbayportal.itaste.ui.fragments.postdetailfragment.itemclicklistener.PostCommentsRvClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_detail.*


@AndroidEntryPoint
class PostDetailFragment : BaseFragment<FragmentPostDetailBinding, PostDetailFragmentViewModel>(), PostCommentsRvClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_post_detail
    override val viewModel: Class<PostDetailFragmentViewModel>
        get() = PostDetailFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun subscribeToNavigationLiveData() {
        super.subscribeToNavigationLiveData()

        mViewModel.onVendorProfileHeaderClicked.observe(this, Observer {
            Navigation.findNavController(ll_dp)
                .navigate(R.id.action_postDetailFragment_to_profileFragment)
        })

        mViewModel.onSendButtonClicked.observe(this, Observer {
            Navigation.findNavController(iv_Chat)
                .navigate(R.id.action_postDetailFragment_to_chatFragment2)
        })

        mViewModel.onBackButtonClicked.observe(this, Observer {
            Navigation.findNavController(img_back).popBackStack()
        })

        mViewModel.onEditPostButtonClicked.observe(this, Observer {
            Navigation.findNavController(btn_edit_post)
                .navigate(R.id.action_postDetailFragment_to_postFragment)
        })

        mViewModel.onMoreButtonClicked.observe(this, Observer {
            Navigation.findNavController(ll_more)
                .navigate(R.id.action_postDetailFragment_to_postDetailBottomSheetFragment)
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.rvPostComments.adapter = PostCommentsAdapter(this)

        ivComment.setOnClickListener(View.OnClickListener {
            layoutComment.visibility = View.VISIBLE
            openSoftKeyboard(requireContext(), view)
        })
    }


    override fun onItemClickListener() {
        Toast.makeText(context, "ItemClicked", Toast.LENGTH_SHORT).show()

    }



}