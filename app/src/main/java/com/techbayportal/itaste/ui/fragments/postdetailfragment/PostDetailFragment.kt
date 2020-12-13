package com.techbayportal.itaste.ui.fragments.postdetailfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.LayoutPostdetailBinding

class PostDetailFragment  : BaseFragment<LayoutPostdetailBinding ,PostDetailViewModel>() {
    override val layoutId: Int
        get() = R.layout.layout_postdetail
    override val viewModel: Class<PostDetailViewModel>
        get() = PostDetailViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel
}