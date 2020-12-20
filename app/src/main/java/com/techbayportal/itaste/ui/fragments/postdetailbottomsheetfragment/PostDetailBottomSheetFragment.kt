package com.techbayportal.itaste.ui.fragments.postdetailbottomsheetfragment

import com.techbayportal.itaste.BR
import com.techbayportal.itaste.R
import com.techbayportal.itaste.baseclasses.BaseFragment
import com.techbayportal.itaste.databinding.FragmentPostDetailBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailBottomSheetFragment : BaseFragment<FragmentPostDetailBottomSheetBinding, PostDetailBottomSheetFragmentViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_post_detail_bottom_sheet
    override val viewModel: Class<PostDetailBottomSheetFragmentViewModel>
        get() = PostDetailBottomSheetFragmentViewModel::class.java
    override val bindingVariable: Int
        get() = BR.viewModel


}