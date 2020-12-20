package com.techbayportal.itaste.ui.fragments.postdetails

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentPostDetailsBinding
import com.techbayportal.itaste.ui.fragments.postdetails.adapter.PostCommentsAdapter
import com.techbayportal.itaste.ui.fragments.postdetails.itemClickListener.PostCommentsRvClickListener
import kotlinx.android.synthetic.main.fragment_post_details.*


class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding, PostDetailViewModel>(), PostCommentsRvClickListener {

    override val layoutId: Int
        get() = R.layout.fragment_post_details
    override val viewModel: Class<PostDetailViewModel>
        get() = PostDetailViewModel::class.java
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