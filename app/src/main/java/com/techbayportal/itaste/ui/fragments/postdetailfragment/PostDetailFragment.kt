package com.techbayportal.itaste.ui.fragments.postdetailfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
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